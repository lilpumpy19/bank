package ru.shchegol.deal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.exception.StatementNotFoundException;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.MessageService;
import ru.shchegol.dto.EmailMessageDto;
import ru.shchegol.dto.enums.Topic;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final KafkaTemplate<String, EmailMessageDto> kafkaTemplate;
    private final StatementRepository statementRepository;

    public void finishRegistration(String statementId) {
        EmailMessageDto emailMessageDto = createMessageDto(statementId, Topic.FINISH_REGISTRATION);
        kafkaTemplate.send("finish-registration", emailMessageDto);
    }


    public void createDocuments(String statementId) {
        EmailMessageDto emailMessageDto = createMessageDto(statementId, Topic.CREATE_DOCUMENTS);
        kafkaTemplate.send("create-documents", emailMessageDto);
    }

    public void sendDocuments(String statementId) {
        EmailMessageDto emailMessageDto = createMessageDto(statementId, Topic.SEND_DOCUMENTS);
        kafkaTemplate.send("send-documents", emailMessageDto);
    }

    public void sendSes(String statementId) {
        EmailMessageDto emailMessageDto = createMessageDto(statementId, Topic.SEND_SES);
        kafkaTemplate.send("send-ses", emailMessageDto);
    }

    public void creditIssued(String statementId) {
        EmailMessageDto emailMessageDto = createMessageDto(statementId, Topic.CREDIT_ISSUED);
        kafkaTemplate.send("credit-issued", emailMessageDto);
    }

    public void statementDenied(String statementId) {
        EmailMessageDto emailMessageDto = createMessageDto(statementId, Topic.STATEMENT_DENIED);
        kafkaTemplate.send("statement-denied", emailMessageDto);
    }
    private EmailMessageDto createMessageDto(String statementId, Topic topic) {
        Optional<Statement> statement = statementRepository.findById(UUID.fromString(statementId));
        if (statement.isPresent()) {
            EmailMessageDto emailMessageDto = new EmailMessageDto();
            emailMessageDto.setAddress(statement.get().getClientId().getEmail());
            emailMessageDto.setTopic(topic);
            emailMessageDto.setStatementId(statementId);
            return emailMessageDto;
        }else {
            throw new StatementNotFoundException(statementId);
        }
    }
}
