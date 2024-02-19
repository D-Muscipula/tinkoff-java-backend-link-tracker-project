package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public record UnregisteredCommand() implements Command {
    private static final String TEXT = "Не зарегистрирован";

    @Override
    public SendMessage handle(Update update) {
        String message = "Вы не зарегистрированы, наберите /start для регистрации";
        return new SendMessage(update.message().chat().id(), message);
    }

    @Override
    public String getTextOfCommand() {
        return TEXT;
    }

    @Override
    public String getDescription() {
        return TEXT;
    }
}
