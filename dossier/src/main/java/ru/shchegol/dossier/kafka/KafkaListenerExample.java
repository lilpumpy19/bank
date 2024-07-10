package ru.shchegol.dossier.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.shchegol.dossier.service.EmailService;
import ru.shchegol.dto.EmailMessageDto;

import javax.mail.MessagingException;

@Component
@RequiredArgsConstructor
public class KafkaListenerExample {
    private final EmailService emailService;
    @KafkaListener(topics = "finish-registration",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "finish-registration");
    }

    @KafkaListener(topics = "create-documents",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener2(EmailMessageDto emailMessageDto) throws MessagingException {
        emailService.sendWithAttachment(emailMessageDto, "create-documents");
    }

    @KafkaListener(topics = "send-documents",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener3(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "send-documents");
    }


    @KafkaListener(topics = "send-ses",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener4(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "send-ses");
    }


    @KafkaListener(topics = "credit-issued",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener5(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "credit-issued");
    }

    @KafkaListener(topics = "statement-denied",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener6(EmailMessageDto emailMessageDto) {
        emailService.send(emailMessageDto, "statement-denied");
    }
}
