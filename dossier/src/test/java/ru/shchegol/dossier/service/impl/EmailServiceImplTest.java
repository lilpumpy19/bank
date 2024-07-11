package ru.shchegol.dossier.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.shchegol.dto.EmailMessageDto;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @InjectMocks
    EmailServiceImpl emailService;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    EmailMessageDto emailMessageDto;

    @BeforeEach
    void setUp() {
        emailService = new EmailServiceImpl(javaMailSender);
        emailMessageDto = new EmailMessageDto();
        emailMessageDto.setAddress("address");
        emailMessageDto.setStatementId("statementId");
    }

    @Test
    void send_ShouldReturnOk() {
        emailService.send(emailMessageDto, "subject");
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendWithAttachment_ShouldReturnOk() throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailMessageDto.setAddress("shchegolevilya1@gmail.com");
        emailService.sendWithAttachment(emailMessageDto, "subject");
        verify(javaMailSender, times(1)).send(eq(mimeMessage));
    }
}