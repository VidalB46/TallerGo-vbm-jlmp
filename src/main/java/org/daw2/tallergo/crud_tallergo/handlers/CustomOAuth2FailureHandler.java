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
 * Manejador de fallos personalizado para el proceso de autenticación OAuth2.
 * Se encarga de limpiar el rastro de la sesión fallida y redirigir al usuario con un mensaje amigable.
 */
@Component
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2FailureHandler.class);

    /**
     * Se ejecuta automáticamente cuando una autenticación OAuth2 (Google, GitHub, etc.) falla.
     * * @param request La petición HTTP.
     * @param response La respuesta HTTP.
     * @param exception La excepción detallada del fallo de autenticación.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        // Logueamos el error para auditoría técnica
        logger.warn("Falló la autenticación OAuth2: {}", exception.getMessage());

        // Garantizamos que no quede rastro de autenticaciones parciales
        SecurityContextHolder.clearContext();

        // Forzamos la invalidación de la sesión para evitar estados inconsistentes
        request.getSession().invalidate();

        /*
         * NOTA: Al invalidar la sesión arriba, debemos crear una nueva o usar flash attributes
         * si queremos que el mensaje sobreviva a la redirección.
         * Aquí, al ser una redirección simple, el atributo se guarda en la nueva sesión.
         */
        request.getSession(true).setAttribute("errorMessage", "El usuario no está registrado en esta aplicación");

        // Devolvemos al usuario a la pantalla de entrada
        response.sendRedirect("/login?error=oauth2");
    }
}