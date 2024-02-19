package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public record Help() implements Command {
    @Override
    public SendMessage handle(Update update) {
        String message = """
            /start - зарегистрировать пользователя
            /help - вывести окно с командами
            /track - начать отслеживание ссылки
            /untrack - прекратить отслеживание ссылки
            /list - показать список отслеживаемых ссылок""";
        return new SendMessage(update.message().chat().id(), message);
    }

    @Override
    public String getTextOfCommand() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "вывести окно с командами";
    }
}
