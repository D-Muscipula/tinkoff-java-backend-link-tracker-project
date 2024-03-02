package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    SendMessage handle(Update update);

    String getTextOfCommand();

    String getDescription();

    default BotCommand toBotCommand() {
        return new BotCommand(getTextOfCommand(), getDescription());
    }

}
