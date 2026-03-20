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
                    logger.debug("Configurando autorización de solicitudes HTTP por Roles");
                    auth
                            // 1. RUTAS PÚBLICAS
                            .requestMatchers("/", "/js/**", "/css/**", "/images/**", "/login", "/register", "/auth/**", "/error", "/error/**").permitAll()

                            // 2. SOLO ADMINISTRADOR: Gestión de personal, usuarios y configuración global
                            .requestMatchers("/users/**").hasRole("ADMIN")
                            .requestMatchers("/mechanics/**").hasRole("ADMIN")
                            .requestMatchers("/workshops/new", "/workshops/edit/**", "/workshops/delete/**").hasRole("ADMIN")

                            // 3. SOLO MECÁNICOS (O ADMIN): Gestión técnica de reparaciones y presupuestos
                            .requestMatchers("/repairs/**").hasAnyRole("MECHANIC", "ADMIN")
                            .requestMatchers("/budgets/**").hasAnyRole("MECHANIC", "ADMIN")

                            // 4. CLIENTES Y STAFF: Gestión de vehículos y citas
                            // El cliente crea citas, el mecánico las ve para trabajar
                            .requestMatchers("/vehicles/**").hasAnyRole("CLIENT", "ADMIN")
                            .requestMatchers("/appointments/**").hasAnyRole("CLIENT", "MECHANIC", "ADMIN")

                            // 5. PERFIL Y REVIEWS: Cualquier usuario autenticado
                            .requestMatchers("/profile/**").authenticated()
                            .requestMatchers("/reviews/add/**").hasRole("CLIENT")

                            // Cualquier otra petición requiere estar logueado
                            .anyRequest().authenticated();
                })
                .formLogin(form -> {
                    logger.debug("Configurando formulario de inicio de sesión");
                    form
                            .loginPage("/login")
                            .defaultSuccessUrl("/")
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