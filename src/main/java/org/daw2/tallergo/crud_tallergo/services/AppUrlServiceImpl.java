package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppUrlServiceImpl implements AppUrlService {

    @Value("${app.public-base-url}")
    private String publicBaseUrl;

    @Value("${app.password-reset.path:/auth/reset-password}")
    private String resetPath;

    @Override
    public String buildResetUrl(String rawToken) {
        return buildUrl(resetPath, Map.of("token", rawToken));
    }

    @Override
    public String buildUrl(String path, Map<String, String> queryParams) {
        UriComponentsBuilder b = UriComponentsBuilder
                .fromUriString(trimTrailingSlash(publicBaseUrl))
                .path(ensureLeadingSlash(path));

        if (queryParams != null) {
            queryParams.forEach(b::queryParam);
        }
        return b.build(true).toUriString();
    }

    private String trimTrailingSlash(String s) {
        if (s == null || s.isBlank()) throw new IllegalStateException("app.public-base-url no está configurada.");
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }

    private String ensureLeadingSlash(String p) {
        if (p == null || p.isBlank()) return "/";
        return p.startsWith("/") ? p : "/" + p;
    }
}