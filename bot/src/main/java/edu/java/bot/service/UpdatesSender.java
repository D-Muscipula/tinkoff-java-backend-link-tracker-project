package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import dto.request.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UpdatesSender {
    private final TelegramBot telegramBot;

    @Autowired
    public UpdatesSender(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendUpdates(LinkUpdate linkUpdate) {
        log.info("обновления получены");
        for (var i : linkUpdate.tgChatIds()) {
            String message = linkUpdate.description();
            telegramBot.execute(new SendMessage(i, message));
        }
    }
}
