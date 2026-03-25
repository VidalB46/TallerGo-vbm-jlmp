package org.daw2.tallergo.crud_tallergo.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.daw2.tallergo.crud_tallergo.entities.Role;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.RoleRepository;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Handler que gestiona el éxito de la autenticación OAuth2.
 * Su función principal es vincular la identidad del proveedor externo (GitHub/Google)
 * con los detalles del usuario local en nuestra base de datos para cargar sus roles y permisos.
 */
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);
    private static final int PASSWORD_EXPIRY_DAYS = 90;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // GitHub puede no exponer el email público — usamos el login como fallback
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            // Fallback: usar login@github.com como email único
            String login = oAuth2User.getAttribute("login");
            email = login + "@github.com";
        }

        // Si el usuario no existe, lo creamos automáticamente con rol ROLE_CLIENT
        if (!userRepository.existsByEmail(email)) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
            newUser.setActive(true);
            newUser.setAccountNonLocked(true);
            newUser.setEmailVerified(true);
            newUser.setMustChangePassword(false);
            newUser.setLastPasswordChange(LocalDateTime.now());
            newUser.setPasswordExpiresAt(LocalDateTime.now().plusDays(PASSWORD_EXPIRY_DAYS));

            Role clientRole = roleRepository.findByName("ROLE_CLIENT")
                    .orElseThrow(() -> new IllegalStateException("ROLE_CLIENT no existe en la BD"));
            newUser.getRoles().add(clientRole);

            userRepository.save(newUser);
            logger.info("Nuevo usuario registrado vía GitHub OAuth2: {}", email);
        }

        // Cargar detalles del usuario (roles) y establecer sesión local
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        logger.info("Login exitoso vía GitHub OAuth2: {}", email);
        response.sendRedirect("/");
    }
}