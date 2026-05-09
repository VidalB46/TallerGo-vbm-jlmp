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

    /**
     * Logger de la clase para trazas de configuración.
     */
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Handler de éxito para autenticación OAuth2.
     */
    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    /**
     * Handler de error para autenticación OAuth2.
     */
    @Autowired
    private CustomOAuth2FailureHandler customOAuth2FailureHandler;

    /**
     * Servicio personalizado de carga de usuarios desde base de datos.
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Define la cadena de filtros de seguridad de la aplicación.
     * Configura rutas públicas, rutas protegidas por rol, formulario de login,
     * autenticación OAuth2 y política de sesiones.
     *
     * @param http Objeto de configuración HTTP de Spring Security.
     * @return Cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
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
                             * IMPORTANTE:
                             * Las rutas específicas de cliente deben ir antes que las genéricas,
                             * para evitar que /budgets/repair/** quede bloqueada por una regla más amplia.
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

    /**
     * Configura el proveedor de autenticación basado en base de datos.
     * Enlaza el servicio personalizado de usuarios con el codificador de contraseñas.
     *
     * @return Proveedor de autenticación configurado.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        logger.info("Entrando en el método authenticationProvider");

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        logger.info("Saliendo del método authenticationProvider");
        return provider;
    }

    /**
     * Registra el codificador BCrypt como bean de Spring.
     *
     * @return Instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Entrando en el método passwordEncoder");
        return new BCryptPasswordEncoder();
    }
}