package org.daw2.tallergo.crud_tallergo.services;

import jakarta.transaction.Transactional;
import org.daw2.tallergo.crud_tallergo.entities.PasswordResetToken;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.PasswordResetTokenRepository;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;

/**
 * Implementación robusta del servicio de recuperación de contraseñas.
 * Utiliza hashing SHA-256 para almacenar los tokens en base de datos,
 * siguiendo las mejores prácticas de seguridad (OWASP).
 */
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    private static final int TOKEN_BYTES = 32;
    private static final int TOKEN_TTL_MINUTES = 45;
    private static final int PASSWORD_EXPIRY_DAYS = 90;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private AppUrlService appUrlService;

    /**
     * Proceso de solicitud:
     * 1. Verifica usuario (sin confirmar existencia por seguridad ante enumeración).
     * 2. Invalida tokens previos.
     * 3. Genera y hashea nuevo token.
     * 4. Envía correo electrónico.
     */
    @Transactional
    @Override
    public void requestPasswordReset(String email, String requestIp, String userAgent) {
        log.info("Solicitud de reset de password para: {} desde IP: {}", email, requestIp);

        Locale locale = LocaleContextHolder.getLocale();
        LocalDateTime now = LocalDateTime.now();

        // Buscamos ignorando case para mayor flexibilidad
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);

        if (user != null) {
            // Seguridad: Un solo token activo a la vez
            tokenRepository.invalidateAllActiveTokensForUser(user.getId(), now);

            // Generación de token criptográficamente seguro
            String rawToken = generateSecureToken();
            String tokenHash = sha256Hex(rawToken);

            PasswordResetToken prt = new PasswordResetToken();
            prt.setUser(user);
            prt.setTokenHash(tokenHash);
            prt.setCreatedAt(now);
            prt.setExpiresAt(now.plusMinutes(TOKEN_TTL_MINUTES));
            prt.setRequestIp(requestIp);
            prt.setUserAgent(safeTruncate(userAgent, 255));

            tokenRepository.save(prt);

            // Construcción del enlace y envío
            String resetUrl = appUrlService.buildResetUrl(rawToken);
            Map<String, Object> vars = Map.of(
                    "resetUrl", resetUrl,
                    "ttlMinutes", TOKEN_TTL_MINUTES,
                    "userEmail", user.getEmail()
            );

            mailService.sendTemplate(
                    user.getEmail(),
                    "mail.passwordreset.subject",
                    "mail/password-reset",
                    vars,
                    locale
            );
        } else {
            // Logueamos pero no informamos al cliente para evitar enumeración de usuarios
            log.warn("Intento de recuperación para email inexistente: {}", email);
        }
    }

    /**
     * Proceso de ejecución:
     * 1. Valida el token contra el hash guardado.
     * 2. Verifica expiración y uso previo.
     * 3. Hashea y guarda la nueva password.
     * 4. Limpia intentos fallidos y desbloquea cuenta.
     */
    @Transactional
    @Override
    public void resetPassword(String rawToken, String newPassword) {
        LocalDateTime now = LocalDateTime.now();
        String tokenHash = sha256Hex(rawToken);

        PasswordResetToken token = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Token de recuperación inválido o inexistente."));

        // Validaciones de seguridad
        if (token.getUsedAt() != null) {
            throw new IllegalStateException("Este token ya ha sido utilizado.");
        }
        if (token.getExpiresAt().isBefore(now)) {
            throw new IllegalStateException("El token de recuperación ha expirado.");
        }

        User user = token.getUser();

        // Actualización de credenciales y estado de cuenta
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setLastPasswordChange(now);
        user.setPasswordExpiresAt(now.plusDays(PASSWORD_EXPIRY_DAYS));
        user.setMustChangePassword(false);
        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(true); // Desbloqueamos si estaba bloqueada por intentos fallidos

        // Consumimos el token
        token.setUsedAt(now);

        userRepository.save(user);
        tokenRepository.save(token);

        log.info("Password actualizada con éxito para el usuario ID: {}", user.getId());
    }

    // --- Helpers de Seguridad ---

    private String generateSecureToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256Hex(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error crítico: Algoritmo SHA-256 no disponible.", e);
        }
    }

    private String safeTruncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}