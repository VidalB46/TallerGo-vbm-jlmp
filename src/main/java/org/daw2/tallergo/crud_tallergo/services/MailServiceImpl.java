package org.daw2.tallergo.crud_tallergo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
 * Servicio genérico de envío de correos de la aplicación.
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from:}")
    private String defaultFrom;

    @Override
    public void sendText(String to, String subject, String text) {
        send(to, subject, text, false);
    }

    @Override
    public void sendHtml(String to, String subject, String html) {
        send(to, subject, html, true);
    }

    @Override
    public void sendTemplate(String to, String subjectKey, String templateName, Map<String, Object> variables, Locale locale) {
        String subject = messageSource.getMessage(subjectKey, null, locale);
        Context ctx = new Context(locale);
        ctx.setVariables(variables);
        ctx.setVariable("subject", subject);
        ctx.setVariable("lang", locale.getLanguage());

        String html = templateEngine.process(templateName, ctx);
        send(to, subject, html, true);
    }

    private void send(String to, String subject, String body, boolean isHtml) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, StandardCharsets.UTF_8.name());

            if (defaultFrom != null && !defaultFrom.isBlank()) {
                helper.setFrom(defaultFrom);
            }

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml);

            mailSender.send(msg);
        } catch (MessagingException e) {
            throw new IllegalStateException("Email could not be sent.", e);
        }
    }
}