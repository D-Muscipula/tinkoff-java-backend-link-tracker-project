package edu.java.bot.kafka;

import dto.request.LinkUpdate;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.UpdatesSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class KafkaUpdatesListener {
    private final ApplicationConfig applicationConfig;
    private final UpdatesSender updatesSender;
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    @Autowired
    public KafkaUpdatesListener(
        ApplicationConfig applicationConfig,
        UpdatesSender updatesSender,
        KafkaTemplate<String, LinkUpdate> kafkaTemplate
    ) {
        this.applicationConfig = applicationConfig;
        this.updatesSender = updatesSender;
        this.kafkaTemplate = kafkaTemplate;
    }

    @RetryableTopic(
        attempts = "1",
        dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(topics = "${app.scrapper-topic.name}")
    public void listen(LinkUpdate update) {
        updatesSender.sendUpdates(update);
    }

    @DltHandler
    public void handleDltLinkUpdate(
        LinkUpdate update
    ) {
        kafkaTemplate.send(applicationConfig.scrapperTopic().name() + "_dlq", update);
        log.info("Event on dlt " + update);
    }
}
