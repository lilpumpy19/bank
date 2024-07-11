package ru.shchegol.dossier.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.shchegol.dossier.service.EmailService;
import ru.shchegol.dto.EmailMessageDto;

import javax.mail.MessagingException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListenerExample {
    private final EmailService emailService;
    @KafkaListener(topics = "finish-registration",
            groupId = "group1",
            containerFactory = "KafkaListenerContainerFactory")
    void listenerFinishRegistration(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "finish-registration");
        log.info("listener: {}", emailMessageDto);
    }

    @KafkaListener(topics = "create-documents",
            groupId = "group1",
            containerFactory = "KafkaListenerContainerFactory")
    void listenerCreateDocuments(EmailMessageDto emailMessageDto){
        emailService.sendWithAttachment(emailMessageDto, "create-documents");
        log.info("listener: {}", emailMessageDto);
    }

    @KafkaListener(topics = "send-documents",
            groupId = "group1",
            containerFactory = "KafkaListenerContainerFactory")
    void listenerSendDocuments(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "send-documents");
        log.info("listener: {}", emailMessageDto);
    }


    @KafkaListener(topics = "send-ses",
            groupId = "group1",
            containerFactory = "KafkaListenerContainerFactory")
    void listenerSendSes(EmailMessageDto emailMessageDto) {
        int randomCode = 100000 + (int) (Math.random() * 900000);;
        emailService.send(emailMessageDto, "ses-code: " + randomCode);
        log.info("listener: {}", emailMessageDto);
    }


    @KafkaListener(topics = "credit-issued",
            groupId = "group1",
            containerFactory = "KafkaListenerContainerFactory")
    void listenerCreditIssued(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "credit-issued");
        log.info("listener: {}", emailMessageDto);
    }

    @KafkaListener(topics = "statement-denied",
            groupId = "group1",
            containerFactory = "KafkaListenerContainerFactory")
    void listenerStatementDenied(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "statement-denied");
        log.info("listener: {}", emailMessageDto);
    }
}
