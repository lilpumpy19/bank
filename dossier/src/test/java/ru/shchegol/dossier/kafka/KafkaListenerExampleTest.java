package ru.shchegol.dossier.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shchegol.dossier.service.EmailService;
import ru.shchegol.dto.EmailMessageDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaListenerExampleTest {
    @InjectMocks
    KafkaListenerExample kafkaListenerExample;
    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp() {
        kafkaListenerExample=new KafkaListenerExample(emailService);
    }

    @Test
    void listenerFinishRegistration_ShouldReturnOk() {
        kafkaListenerExample.listenerFinishRegistration(new EmailMessageDto());
        verify(emailService, times(1)).send(any(EmailMessageDto.class), any(String.class));
    }

    @Test
    void listenerCreateDocuments_ShouldReturnOk() {
        kafkaListenerExample.listenerCreateDocuments(new EmailMessageDto());
        verify(emailService, times(1)).sendWithAttachment(any(EmailMessageDto.class), any(String.class));
    }

    @Test
    void listenerSendDocuments_ShouldReturnOk() {
        kafkaListenerExample.listenerSendDocuments(new EmailMessageDto());
        verify(emailService, times(1)).send(any(EmailMessageDto.class), any(String.class));
    }

    @Test
    void listenerSendSes_ShouldReturnOk() {
        kafkaListenerExample.listenerSendSes(new EmailMessageDto());
        verify(emailService, times(1)).send(any(EmailMessageDto.class), any(String.class));
    }

    @Test
    void listenerCreditIssued_ShouldReturnOk() {
        kafkaListenerExample.listenerCreditIssued(new EmailMessageDto());
        verify(emailService, times(1)).send(any(EmailMessageDto.class), any(String.class));
    }

    @Test
    void listenerStatementDenied_ShouldReturnOk() {
        kafkaListenerExample.listenerStatementDenied(new EmailMessageDto());
        verify(emailService, times(1)).send(any(EmailMessageDto.class), any(String.class));
    }
}