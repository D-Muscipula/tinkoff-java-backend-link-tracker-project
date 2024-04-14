package edu.java.scrapper.domain.service.updater;

import dto.request.LinkUpdate;
import edu.java.scrapper.domain.service.kafka_queue_producer.ScrapperQueueProducer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
@Service
public class KafkaUpdateSender implements UpdateSender {
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final Counter updatesCounter;

    @Autowired
    public KafkaUpdateSender(ScrapperQueueProducer scrapperQueueProducer, MeterRegistry meterRegistry) {
        updatesCounter = meterRegistry.counter("count_of_update_sends");
        this.scrapperQueueProducer = scrapperQueueProducer;
    }

    @Override
    public void sendUpdate(LinkUpdate linkUpdate) {
        updatesCounter.increment();
        scrapperQueueProducer.send(linkUpdate);
    }
}
