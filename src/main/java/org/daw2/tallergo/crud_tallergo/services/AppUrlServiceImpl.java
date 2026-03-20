package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

/**
 * Implementación del servicio de construcción de URLs.
 * Utiliza {@link UriComponentsBuilder} para garantizar que las URLs generadas
 * estén correctamente codificadas y sigan un formato válido, evitando errores
 * comunes de concatenación manual.
 */
@Service
@RequiredArgsConstructor
public class AppUrlServiceImpl implements AppUrlService {

    /**
     * URL base pública de la aplicación (ej: https://tallergo.com).
     * Se inyecta desde el archivo de propiedades (application.yml).
     */
    @Value("${app.public-base-url}")
    private String publicBaseUrl;

    /**
     * Ruta específica para el formulario de reset de contraseña.
     * Tiene un valor por defecto si no se configura en las propiedades.
     */
    @Value("${app.password-reset.path:/auth/reset-password}")
    private String resetPath;

    /**
     * Genera la URL de recuperación de contraseña adjuntando el token como query param.
     */
    @Override
    public String buildResetUrl(String rawToken) {
        return buildUrl(resetPath, Map.of("token", rawToken));
    }

    /**
     * Construye una URL absoluta combinando la base, la ruta y los parámetros de consulta.
     * Maneja automáticamente la limpieza de slashes (barras) para evitar "//" en la URL final.
     */
    @Override
    public String buildUrl(String path, Map<String, String> queryParams) {
        UriComponentsBuilder b = UriComponentsBuilder
                .fromUriString(trimTrailingSlash(publicBaseUrl))
                .path(ensureLeadingSlash(path));

        if (queryParams != null) {
            queryParams.forEach(b::queryParam);
        }

        // build(true) indica que los componentes ya están codificados si fuera necesario
        return b.build(true).toUriString();
    }

    /**
     * Elimina la barra final de la URL base para unificar el formato.
     */
    private String trimTrailingSlash(String s) {
        if (s == null || s.isBlank()) {
            throw new IllegalStateException("La propiedad 'app.public-base-url' no está configurada en el entorno.");
        }
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }

    /**
     * Asegura que la ruta relativa comience con una barra.
     */
    private String ensureLeadingSlash(String p) {
        if (p == null || p.isBlank()) return "/";
        return p.startsWith("/") ? p : "/" + p;
    }
}