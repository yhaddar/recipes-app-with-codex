package com.recipemanager.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnBean(JavaMailSender.class)
public class SmtpEmailService {

    private final JavaMailSender mailSender;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(EmailMessage message) {
        if (message == null || message.recipient() == null || message.recipient().isBlank()) {
            throw new IllegalArgumentException("Email recipient is required");
        }
        if (message.subject() == null || message.subject().isBlank()) {
            throw new IllegalArgumentException("Email subject is required");
        }
        if (message.text() == null || message.text().isBlank()) {
            throw new IllegalArgumentException("Email text is required");
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(message.recipient());
        mail.setSubject(message.subject());
        mail.setText(message.text());
        mailSender.send(mail);
    }
}
