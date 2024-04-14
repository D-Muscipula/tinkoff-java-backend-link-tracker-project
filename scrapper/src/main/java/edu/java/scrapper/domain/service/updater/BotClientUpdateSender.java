package edu.java.scrapper.domain.service.updater;

import dto.request.LinkUpdate;
import edu.java.scrapper.client.BotClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(name = "app.use-queue", havingValue = "false")
@Service
public class BotClientUpdateSender implements UpdateSender {
    private final BotClient botClient;
    private final Counter updatesCounter;

    @Autowired
    public BotClientUpdateSender(BotClient botClient, MeterRegistry meterRegistry) {
        this.botClient = botClient;
        updatesCounter = meterRegistry.counter("count_of_update_sends");
    }

    @Override
    public void sendUpdate(LinkUpdate linkUpdate) {
        updatesCounter.increment();
        botClient.sendUpdate(linkUpdate);
    }
}
