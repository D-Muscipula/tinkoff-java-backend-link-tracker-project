package edu.java.scrapper.domain.service.updater;

import dto.request.LinkUpdate;
import edu.java.scrapper.client.BotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(name = "app.use-queue", havingValue = "false")
@Service
public class BotClientUpdateSender implements UpdateSender {
    private final BotClient botClient;

    @Autowired
    public BotClientUpdateSender(BotClient botClient) {
        this.botClient = botClient;
    }

    @Override
    public void sendUpdate(LinkUpdate linkUpdate) {
        botClient.sendUpdate(linkUpdate);
    }
}
