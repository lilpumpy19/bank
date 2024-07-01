package ru.shchegol.deal.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    @Bean
    public NewTopic finishRegistration() {
        return TopicBuilder.name("finish-registration")
                .partitions(1)
                .build();
    }
}
