package org.daw2.tallergo.crud_tallergo.config;

// IMPORTANTE: Descomentar estos imports cuando creemos las clases en los siguientes pasos
// import org.daw2.tallergo.crud_tallergo.handlers.CustomOAuth2FailureHandler;
// import org.daw2.tallergo.crud_tallergo.handlers.CustomOAuth2SuccessHandler;
// import org.daw2.tallergo.crud_tallergo.services.CustomUserDetailsService;

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
 * Configura la seguridad de la aplicación TallerGo, definiendo autenticación y autorización
 * para diferentes roles de usuario, y gestionando la política de sesiones.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  // Activa la seguridad basada en métodos
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // TODO: Descomentar cuando existan en la carpeta 'handlers' y 'services'
    // @Autowired
    // private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    //
    // @Autowired
    // private CustomOAuth2FailureHandler customOAuth2FailureHandler;
    //
    // @Autowired
    // private CustomUserDetailsService customUserDetailsService;

    /**
     * Configura el filtro de seguridad para las solicitudes HTTP, especificando las
     * rutas permitidas y los roles necesarios para acceder a diferentes endpoints.
     *
     * @param http instancia de {@link HttpSecurity} para configurar la seguridad.
     * @return una instancia de {@link SecurityFilterChain} que contiene la configuración de seguridad.
     * @throws Exception si ocurre un error en la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Entrando en el método securityFilterChain");

        // Configuración de seguridad adaptada a TallerGo
        http
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configurando autorización de solicitudes HTTP");
                    auth
                            .requestMatchers("/", "/js/**", "/css/**", "/images/**", "/login", "/register", "/auth/**","/error", "/error/**").permitAll() // Acceso anónimo
                            .requestMatchers("/workshops**", "/services**").hasRole("ADMIN")      // Solo ADMIN gestiona talleres y catálogo
                            .requestMatchers("/vehicles**", "/appointments**").hasAnyRole("CLIENT", "ADMIN") // CLIENTES y ADMINS pueden ver vehículos/citas
                            .anyRequest().authenticated();           // Cualquier otra solicitud requiere autenticación
                })
                .formLogin(form -> {
                    logger.debug("Configurando formulario de inicio de sesión");
                    form
                            .loginPage("/login") // Página personalizada de login
                            .defaultSuccessUrl("/") // Redirige al inicio después del login
                            .permitAll();
                })
                /* TODO: Descomentar cuando tengamos implementado OAuth2
                .oauth2Login(oauth2 -> {
                    logger.debug("Configurando login con OAuth2");
                    oauth2
                            .loginPage("/login")        // Reutiliza la página de inicio de sesión personalizada
                            .successHandler(customOAuth2SuccessHandler) // Usa el Success Handler personalizado
                            .failureHandler(customOAuth2FailureHandler); // Handler para fallo en autenticación
                })
                */
                .sessionManagement(session -> {
                    logger.debug("Configurando política de gestión de sesiones");
                    // Usa sesiones cuando sea necesario
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                });

        logger.info("Saliendo del método securityFilterChain");
        return http.build();
    }

    /**
     * Provider de autenticación basado en DAO.
     *
     * <p>Usa el {@link CustomUserDetailsService} para localizar usuarios en BD y el
     * {@link PasswordEncoder} para verificar la contraseña (BCrypt).</p>
     *
     * @return {@link DaoAuthenticationProvider} configurado.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        logger.info("Entrando en el método authenticationProvider");

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // TODO: Descomentar cuando tengamos CustomUserDetailsService
        // provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        logger.info("Saliendo del método authenticationProvider");
        return provider;
    }

    /**
     * Configura el codificador de contraseñas para cifrar las contraseñas de los usuarios
     * utilizando BCrypt.
     *
     * @return una instancia de {@link PasswordEncoder} que utiliza BCrypt para cifrar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Entrando en el método passwordEncoder");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Saliendo del método passwordEncoder");
        return encoder;
    }
}