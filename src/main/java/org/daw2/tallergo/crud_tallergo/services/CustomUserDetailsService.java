package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Servicio encargado de la carga de usuarios para el motor de Spring Security.
 * Implementa la interfaz UserDetailsService para permitir que la aplicación
 * valide credenciales contra nuestra base de datos.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Localiza al usuario basado en su email (username) y construye el objeto UserDetails.
     * Se marca como @Transactional para asegurar que la carga de roles (Lazy) funcione
     * si no se usara EntityGraph en el repositorio.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando cargar detalles de usuario para: {}", username);

        // 1. Buscar el usuario en la base de datos
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("Fallo de autenticación: No se encontró el email {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + username);
                });

        // 2. Mapear nuestra entidad User al objeto UserDetails de Spring Security
        // Usamos el builder nativo de Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash()) // Password hasheada (BCrypt normalmente)
                .authorities(
                        user.getRoles().stream()
                                .map(role -> role.getName()) // Extraemos nombres (ej: ROLE_ADMIN)
                                .toArray(String[]::new)
                )
                .accountExpired(false)
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(false)
                .disabled(!user.isActive()) // Si active es false, el login fallará
                .build();
    }
}