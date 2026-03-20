package org.daw2.tallergo.crud_tallergo.services;

import java.util.Map;

/**
 * Interfaz para la gestión centralizada de URLs de la aplicación.
 * Permite construir enlaces absolutos para correos electrónicos, redirecciones
 * y comunicaciones externas (como los tokens de recuperación de contraseña).
 */
public interface AppUrlService {

    /**
     * Construye la URL completa para el formulario de restablecimiento de contraseña.
     * * @param rawToken El token en texto plano que se enviará al usuario.
     * @return URL absoluta (ej: "https://tallergo.com/auth/reset-password?token=abc-123").
     */
    String buildResetUrl(String rawToken);

    /**
     * Método genérico para construir URLs con parámetros de consulta dinámicos.
     * * @param path El endpoint o ruta relativa (ej: "/confirm-email").
     * @param queryParams Mapa de pares clave-valor para la query string.
     * @return URL absoluta formateada correctamente.
     */
    String buildUrl(String path, Map<String, String> queryParams);
}