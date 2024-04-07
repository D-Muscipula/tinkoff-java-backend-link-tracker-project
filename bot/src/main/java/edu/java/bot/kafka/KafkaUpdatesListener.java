package edu.java.bot.kafka;

import dto.request.LinkUpdate;
import edu.java.bot.service.UpdatesSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaUpdatesListener {
    private final UpdatesSender updatesSender;

    @Autowired
    public KafkaUpdatesListener(UpdatesSender updatesSender) {
        this.updatesSender = updatesSender;
    }

    @KafkaListener(topics = "${app.scrapper-topic.name}")
    public void listen(LinkUpdate update) {
        updatesSender.sendUpdates(update);
    }
}
