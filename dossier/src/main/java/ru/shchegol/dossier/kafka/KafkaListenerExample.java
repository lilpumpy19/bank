package ru.shchegol.dossier.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.shchegol.dto.EmailMassageDto;

@Component
public class KafkaListenerExample {
    @KafkaListener(topics = "finish-registration",
            groupId = "group1",
            containerFactory = "finishRegistrationKafkaListenerContainerFactory")
    void listener(EmailMassageDto emailMassageDto) {
        System.out.println(emailMassageDto);
    }
}
