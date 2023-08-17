package com.example.NotesApp.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;


@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfig;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, Configuration freemarkerConfig) {
        this.javaMailSender = javaMailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    public void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> model) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            Template template = freemarkerConfig.getTemplate(templateName + ".ftlh");
            String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(emailContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | IOException | TemplateException e) {
            // Обработка ошибок
        }
    }
}

