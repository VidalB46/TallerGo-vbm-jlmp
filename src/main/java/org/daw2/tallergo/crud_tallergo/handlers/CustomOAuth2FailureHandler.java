package org.daw2.tallergo.crud_tallergo.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler personalizado para manejar fallos en la autenticación con OAuth2.
 */
@Component
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2FailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        logger.warn("Falló la autenticación OAuth2: {}", exception.getMessage());

        // Limpiar el contexto de seguridad
        SecurityContextHolder.clearContext();

        // Invalidar la sesión actual
        request.getSession().invalidate();

        // Agregar el mensaje de error como un atributo de sesión
        request.getSession().setAttribute("errorMessage", "El usuario no está registrado en esta aplicación");

        // Redirigir al login
        response.sendRedirect("/login");
    }
}