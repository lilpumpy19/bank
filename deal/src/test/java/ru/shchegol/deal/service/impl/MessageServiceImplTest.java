package ru.shchegol.deal.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.exception.StatementNotFoundException;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.dto.EmailMessageDto;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private KafkaTemplate<String, EmailMessageDto> kafkaTemplate;
    @Mock
    private StatementRepository statementRepository;
    @InjectMocks
    private MessageServiceImpl messageService;
    @Mock
    Statement statement;
    @Mock
    Client client;


    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl(kafkaTemplate, statementRepository);
        statement = new Statement();
        statement.setClientId(client);
    }

    @Test
    void finishRegistration_ShouldReturnOk() throws StatementNotFoundException {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        messageService.finishRegistration(UUID.randomUUID().toString());
        verify(kafkaTemplate, times(1)).send(eq("finish-registration"), any(EmailMessageDto.class));
    }

    @Test
    void createDocuments_ShouldReturnOk() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        messageService.createDocuments(UUID.randomUUID().toString());
        verify(kafkaTemplate,times(1)).send(eq("create-documents"), any(EmailMessageDto.class));
    }

    @Test
    void sendDocuments_ShouldReturnOk() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        messageService.sendDocuments(UUID.randomUUID().toString());
        verify(kafkaTemplate,times(1)).send(eq("send-documents"), any(EmailMessageDto.class));
    }

    @Test
    void sendSes_ShouldReturnOk() {
       when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
       messageService.sendSes(UUID.randomUUID().toString());
       verify(kafkaTemplate,times(1)).send(eq("send-ses"), any(EmailMessageDto.class));
    }

    @Test
    void creditIssued_ShouldReturnOk() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        messageService.creditIssued(UUID.randomUUID().toString());
        verify(kafkaTemplate,times(1)).send(eq("credit-issued"), any(EmailMessageDto.class));
    }

    @Test
    void statementDenied_ShouldReturnOk() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        messageService.statementDenied(UUID.randomUUID().toString());
        verify(kafkaTemplate,times(1)).send(eq("statement-denied"), any(EmailMessageDto.class));
    }
}