package edu.java.scrapper.domain.service.kafka_queue_producer;

import dto.request.LinkUpdate;
import edu.java.scrapper.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public ScrapperQueueProducer(KafkaTemplate<String, LinkUpdate> kafkaTemplate, ApplicationConfig applicationConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationConfig = applicationConfig;
    }

    public void send(LinkUpdate update) {
        kafkaTemplate.send(applicationConfig.kafkaTopicName(), update);
    }
}
