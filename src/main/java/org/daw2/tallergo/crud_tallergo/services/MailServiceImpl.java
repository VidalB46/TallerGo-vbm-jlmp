package org.daw2.tallergo.crud_tallergo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * Implementación del servicio de mensajería.
 * Integra JavaMailSender con el motor de plantillas Thymeleaf para enviar
 * correos dinámicos, multi-idioma y con soporte HTML.
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    /**
     * Dirección de correo remitente configurada en application.yml.
     */
    @Value("${spring.mail.from:noreply@tallergo.com}")
    private String defaultFrom;

    /**
     * Envía un correo en texto plano.
     *
     * @param to      Dirección de correo del destinatario.
     * @param subject Asunto del mensaje.
     * @param text    Cuerpo del mensaje en texto plano.
     */
    @Override
    public void sendText(String to, String subject, String text) {
        send(to, subject, text, false);
    }

    /**
     * Envía un correo con cuerpo HTML.
     *
     * @param to      Dirección de correo del destinatario.
     * @param subject Asunto del mensaje.
     * @param html    Cuerpo del mensaje en formato HTML.
     */
    @Override
    public void sendHtml(String to, String subject, String html) {
        send(to, subject, html, true);
    }

    /**
     * Procesa una plantilla Thymeleaf, traduce el asunto y envía el correo resultante.
     *
     * @param to           Dirección de correo del destinatario.
     * @param subjectKey   Clave i18n del asunto (buscada en {@code messages*.properties}).
     * @param templateName Nombre de la plantilla Thymeleaf (sin extensión {@code .html}).
     * @param variables    Variables dinámicas inyectadas en el contexto de la plantilla.
     * @param locale       Idioma del destinatario, usado para traducción y plantilla.
     */
    @Override
    public void sendTemplate(String to, String subjectKey, String templateName, Map<String, Object> variables, Locale locale) {
        // 1. Obtener el asunto traducido desde messages.properties
        String subject = messageSource.getMessage(subjectKey, null, locale);

        // 2. Preparar el contexto de Thymeleaf con las variables dinámicas
        Context ctx = new Context(locale);
        ctx.setVariables(variables);
        ctx.setVariable("subject", subject);
        ctx.setVariable("lang", locale.getLanguage());

        // 3. Renderizar el HTML final
        String html = templateEngine.process(templateName, ctx);

        // 4. Enviar
        send(to, subject, html, true);
    }

    /**
     * Método privado de bajo nivel para la construcción y envío del {@link MimeMessage}.
     *
     * @param to     Dirección de correo del destinatario.
     * @param subject Asunto del mensaje.
     * @param body   Cuerpo del mensaje (texto plano o HTML).
     * @param isHtml {@code true} para enviar el cuerpo como HTML; {@code false} para texto plano.
     * @throws IllegalStateException si ocurre un error al construir o enviar el mensaje.
     */
    private void send(String to, String subject, String body, boolean isHtml) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            // MimeMessageHelper facilita la configuración de adjuntos, HTML y codificación
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, StandardCharsets.UTF_8.name());

            helper.setFrom(defaultFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml);

            mailSender.send(msg);
            log.info("Correo enviado con éxito a: {} | Asunto: {}", to, subject);

        } catch (MessagingException e) {
            log.error("Error al construir o enviar el correo a {}: {}", to, e.getMessage());
            throw new IllegalStateException("No se pudo enviar el correo electrónico.", e);
        }
    }
}