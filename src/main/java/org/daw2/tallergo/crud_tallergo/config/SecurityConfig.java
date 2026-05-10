package org.daw2.tallergo.crud_tallergo.config;

import org.daw2.tallergo.crud_tallergo.handlers.CustomOAuth2FailureHandler;
import org.daw2.tallergo.crud_tallergo.handlers.CustomOAuth2SuccessHandler;
import org.daw2.tallergo.crud_tallergo.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración central de Spring Security para TallerGO.
 * Define las reglas de acceso por rol, el proveedor de autenticación
 * basado en base de datos, el soporte de login con OAuth2 y la gestión
 * de sesiones de la aplicación.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Autowired
    private CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Entrando en el método securityFilterChain");

        http
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configurando autorización de solicitudes HTTP por roles");

                    auth
                            /*
                             * 1. RUTAS PÚBLICAS
                             */
                            .requestMatchers(
                                    "/",
                                    "/js/**",
                                    "/css/**",
                                    "/images/**",
                                    "/login",
                                    "/register",
                                    "/auth/**",
                                    "/error",
                                    "/error/**",
                                    "/lang"
                            ).permitAll()

                            /*
                             * 2. RUTAS DE ADMINISTRACIÓN GLOBAL
                             */
                            .requestMatchers("/users/**").hasRole("ADMIN")
                            .requestMatchers("/workshops/new", "/workshops/edit/**", "/workshops/delete/**").hasRole("ADMIN")

                            /*
                             * 3. RUTAS DE TALLERES
                             */
                            .requestMatchers("/workshops/**").hasAnyRole("CLIENT", "ADMIN")

                            /*
                             * 4. MECÁNICOS Y GESTIÓN INTERNA DEL TALLER
                             */
                            .requestMatchers("/mechanics/**").hasAnyRole("ADMIN", "WORKSHOP_ADMIN")

                            /*
                             * 5. REPARACIONES Y PRESUPUESTOS
                             */
                            .requestMatchers("/repairs/**").hasAnyRole("ADMIN", "WORKSHOP_ADMIN")
                            .requestMatchers("/budgets/repair/**").hasAnyRole("CLIENT", "ADMIN", "WORKSHOP_ADMIN")
                            .requestMatchers("/budgets/*/accept", "/budgets/*/reject").hasAnyRole("CLIENT", "ADMIN", "WORKSHOP_ADMIN")
                            .requestMatchers("/budgets/new").hasAnyRole("ADMIN", "WORKSHOP_ADMIN")

                            /*
                             * 6. VEHÍCULOS Y CITAS
                             */
                            .requestMatchers("/vehicles/**").hasAnyRole("CLIENT", "ADMIN")
                            .requestMatchers("/appointments/**").hasAnyRole("CLIENT", "ADMIN", "WORKSHOP_ADMIN")

                            /*
                             * 7. PERFIL DE USUARIO
                             */
                            .requestMatchers("/profile/**").authenticated()

                            /*
                             * 8. RESEÑAS
                             */
                            .requestMatchers("/reviews/my").hasRole("WORKSHOP_ADMIN")
                            .requestMatchers("/reviews/workshop/**").authenticated()
                            .requestMatchers("/reviews/new").hasRole("CLIENT")
                            .requestMatchers("/reviews/*/delete").hasAnyRole("CLIENT", "ADMIN")

                            /*
                             * 9. CUALQUIER OTRA RUTA REQUIERE AUTENTICACIÓN
                             */
                            .anyRequest().authenticated();
                })
                .formLogin(form -> {
                    logger.debug("Configurando formulario de inicio de sesión");
                    form
                            .loginPage("/login")
                            .defaultSuccessUrl("/", true)
                            .permitAll();
                })
                .oauth2Login(oauth2 -> {
                    logger.debug("Configurando login con OAuth2");
                    oauth2
                            .loginPage("/login")
                            .successHandler(customOAuth2SuccessHandler)
                            .failureHandler(customOAuth2FailureHandler);
                })
                .sessionManagement(session -> {
                    logger.debug("Configurando política de gestión de sesiones");
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                });

        logger.info("Saliendo del método securityFilterChain");
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        logger.info("Entrando en el método authenticationProvider");

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        logger.info("Saliendo del método authenticationProvider");
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Entrando en el método passwordEncoder");
        return new BCryptPasswordEncoder();
    }
}