package org.daw2.tallergo.crud_tallergo.services;

import java.util.Locale;
import java.util.Map;

public interface MailService {
    void sendText(String to, String subject, String text);
    void sendHtml(String to, String subject, String html);
    void sendTemplate(String to,
                      String subjectKey,
                      String templateName,
                      Map<String, Object> variables,
                      Locale locale);
}