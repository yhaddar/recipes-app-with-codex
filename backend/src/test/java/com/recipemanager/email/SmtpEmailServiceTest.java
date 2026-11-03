package com.recipemanager.email;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;

class SmtpEmailServiceTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final SmtpEmailService service = new SmtpEmailService(mailSender);

    @Test
    void sendsValidatedEmailThroughSmtpSender() {
        service.send(new EmailMessage("person@example.test", "Welcome", "Hello"));

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();
        assertThat(sent.getTo()).containsExactly("person@example.test");
        assertThat(sent.getSubject()).isEqualTo("Welcome");
        assertThat(sent.getText()).isEqualTo("Hello");
    }

    @Test
    void rejectsMissingRecipient() {
        assertThatThrownBy(() -> service.send(new EmailMessage("", "Subject", "Text")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email recipient is required");
    }
}
