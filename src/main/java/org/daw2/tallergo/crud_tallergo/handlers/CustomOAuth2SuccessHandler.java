package org.daw2.tallergo.crud_tallergo.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler que gestiona el éxito de la autenticación OAuth2.
 * Su función principal es vincular la identidad del proveedor externo (GitHub/Google)
 * con los detalles del usuario local en nuestra base de datos para cargar sus roles y permisos.
 */
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Se ejecuta tras un login exitoso en el proveedor externo.
     * Valida que el usuario exista en TallerGo y establece la sesión local.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Para GitHub se suele usar "login", para Google/Keycloak suele ser "email".
        String username = oAuth2User.getAttribute("login");

        // 1. Validar existencia del usuario en nuestra DB
        if (username == null || !userRepository.existsByEmail(username)) {
            // Este error será capturado por el CustomOAuth2FailureHandler que definimos antes
            throw new OAuth2AuthenticationException("El usuario " + username + " no está registrado en el sistema.");
        }

        // 2. Cargar los detalles del usuario (incluyendo sus roles/permisos locales)
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // 3. Reemplazar el token de OAuth2 por uno interno de Spring Security
        // Esto permite que el resto de la App use UserDetails en lugar de OAuth2User
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        // 4. Actualizar el contexto de seguridad con la identidad local
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 5. Redirigir a la página principal tras el login exitoso
        response.sendRedirect("/");
    }
}