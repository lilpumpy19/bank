package ru.shchegol.dossier.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.shchegol.dto.EmailMessageDto;

@Component
public class KafkaListenerExample {
    @KafkaListener(topics = "finish-registration",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener(EmailMessageDto emailMessageDto) {
        System.out.println(emailMessageDto);
    }

    @KafkaListener(topics = "create-documents",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener2(EmailMessageDto emailMessageDto) {
        System.out.println(emailMessageDto);
    }

    @KafkaListener(topics = "send-documents",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener3(EmailMessageDto emailMessageDto) {
        System.out.println(emailMessageDto);
    }


    @KafkaListener(topics = "send-ses",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener4(EmailMessageDto emailMessageDto) {
        System.out.println(emailMessageDto);
    }


    @KafkaListener(topics = "credit-issued",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener5(EmailMessageDto emailMessageDto) {
        System.out.println(emailMessageDto);
    }

    @KafkaListener(topics = "statement-denied",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener6(EmailMessageDto emailMessageDto) {
        System.out.println(emailMessageDto);
    }
}
