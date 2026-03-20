package org.daw2.tallergo.crud_tallergo.services;

import java.util.Locale;
import java.util.Map;

/**
 * Interfaz para el servicio de mensajería electrónica de TallerGo.
 * Define los métodos necesarios para notificaciones internas, recuperación de contraseñas
 * y comunicación con clientes.
 */
public interface MailService {

    /**
     * Envía un correo electrónico en formato de texto plano (Simple Mail).
     * @param to Destinatario.
     * @param subject Asunto del mensaje.
     * @param text Cuerpo del mensaje sin formato.
     */
    void sendText(String to, String subject, String text);

    /**
     * Envía un correo electrónico permitiendo etiquetas HTML para diseño.
     * @param to Destinatario.
     * @param subject Asunto del mensaje.
     * @param html Contenido formateado en HTML.
     */
    void sendHtml(String to, String subject, String html);

    /**
     * Envía un correo procesando una plantilla (usualmente Thymeleaf) con internacionalización.
     * Es el método preferido para correos corporativos como "Bienvenida" o "Reset Password".
     * * @param to Destinatario.
     * @param subjectKey Clave de traducción para el asunto (messages.properties).
     * @param templateName Nombre del archivo de plantilla (ej: "mail/reset-password").
     * @param variables Mapa con los datos dinámicos para la plantilla.
     * @param locale Idioma en el que se debe renderizar el correo.
     */
    void sendTemplate(String to,
                      String subjectKey,
                      String templateName,
                      Map<String, Object> variables,
                      Locale locale);
}