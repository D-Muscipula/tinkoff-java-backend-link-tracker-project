package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public record UnknownCommand() implements Command {
    static String commandText = "Неизвестная команда";

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Неизвестная комманда");
    }

    @Override
    public String getTextOfCommand() {
        return commandText;
    }

    @Override
    public String getDescription() {
        return commandText;
    }
}
