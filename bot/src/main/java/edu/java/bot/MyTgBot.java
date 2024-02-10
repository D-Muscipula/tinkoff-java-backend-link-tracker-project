package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTgBot {
    private final Logger logger = LoggerFactory.getLogger(MyTgBot.class);
    private final TelegramBot telegramBot;
    private final MessageHandler messageHandler;

    public MyTgBot(TelegramBot telegramBot, MessageHandler messageHandler) {
        this.telegramBot = telegramBot;
        this.messageHandler = messageHandler;
    }

    public void serve() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach((update) -> {
                logger.info("Update: " + update);
                SendMessage botAnswer = messageHandler.handleMessage(update);
                if (botAnswer != null) {
                    SendResponse response = telegramBot.execute(botAnswer);
                    logger.info("response: " + response);
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                e.printStackTrace();
            }
        });
    }
}
