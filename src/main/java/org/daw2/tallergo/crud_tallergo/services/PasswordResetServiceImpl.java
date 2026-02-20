package org.daw2.tallergo.crud_tallergo.services;

import jakarta.transaction.Transactional;
import org.daw2.tallergo.crud_tallergo.entities.PasswordResetToken;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.PasswordResetTokenRepository;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
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
 * Servicio de recuperación de contraseña basado en tokens de un solo uso.
 */
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

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

    @Transactional
    @Override
    public void requestPasswordReset(String email, String requestIp, String userAgent) {

        Locale locale = LocaleContextHolder.getLocale();
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);

        if (user != null) {
            // Invalida tokens anteriores
            tokenRepository.invalidateAllActiveTokensForUser(user.getId(), now);

            // Genera token aleatorio y guarda hash
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

            // Construye URL y envía mail
            String resetUrl = appUrlService.buildResetUrl(rawToken);

            Map<String, Object> vars = Map.of(
                    "resetUrl", resetUrl,
                    "ttlMinutes", TOKEN_TTL_MINUTES
            );

            mailService.sendTemplate(
                    user.getEmail(),
                    "mail.passwordreset.subject",
                    "mail/password-reset",
                    vars,
                    locale
            );
        }
    }

    @Transactional
    @Override
    public void resetPassword(String rawToken, String newPassword) {

        LocalDateTime now = LocalDateTime.now();
        String tokenHash = sha256Hex(rawToken);

        PasswordResetToken token = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (token.isUsed() || token.isExpired()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setLastPasswordChange(now);
        user.setPasswordExpiresAt(now.plusDays(PASSWORD_EXPIRY_DAYS));
        user.setMustChangePassword(Boolean.FALSE);
        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(Boolean.TRUE);

        token.setUsedAt(now);

        userRepository.save(user);
        tokenRepository.save(token);
    }

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
            throw new IllegalStateException(e);
        }
    }

    private String safeTruncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}