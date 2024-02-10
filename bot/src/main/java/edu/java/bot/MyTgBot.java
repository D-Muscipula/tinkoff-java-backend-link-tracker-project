package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.message_handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class MyTgBot {
    private final Logger logger = LoggerFactory.getLogger(MyTgBot.class);
    private final TelegramBot telegramBot;
    private final MessageHandler messageHandler;
    private final List<Command> commandList;

    public MyTgBot(TelegramBot telegramBot, MessageHandler messageHandler, List<Command> commandList) {
        this.telegramBot = telegramBot;
        this.messageHandler = messageHandler;
        this.commandList = commandList;
    }

    public void serve() {
        //Добавление команд в меню
        BotCommand[] botCommandList = commandList.stream()
                .map(Command::toBotCommand)
                .toArray(BotCommand[]::new);
        telegramBot.execute(new SetMyCommands(botCommandList));

        telegramBot.setUpdatesListener(updates -> {
            updates.forEach((update) -> {
                logger.info("Update: " + update);
                logger.info("Reply: " + update.message().replyToMessage());
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
