package edu.java.scrapper.domain.service.updater;

import dto.request.LinkUpdate;
import edu.java.scrapper.domain.service.kafka_queue_producer.ScrapperQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
@Service
public class KafkaUpdateSender implements UpdateSender {
    private final ScrapperQueueProducer scrapperQueueProducer;

    @Autowired
    public KafkaUpdateSender(ScrapperQueueProducer scrapperQueueProducer) {
        this.scrapperQueueProducer = scrapperQueueProducer;
    }

    @Override
    public void sendUpdate(LinkUpdate linkUpdate) {
        scrapperQueueProducer.send(linkUpdate);
    }
}
