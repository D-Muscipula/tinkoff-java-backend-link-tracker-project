package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public record UnknownCommand() implements Command {
    private final static String COMMAND_TEXT = "Неизвестная команда";

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Неизвестная комманда");
    }

    @Override
    public String getTextOfCommand() {
        return COMMAND_TEXT;
    }

    @Override
    public String getDescription() {
        return COMMAND_TEXT;
    }
}
