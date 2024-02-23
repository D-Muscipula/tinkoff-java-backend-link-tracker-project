package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.User;

public record Start(User user) implements Command {
    @Override
    public SendMessage handle(Update update) {
        String message = "Приветствую! Данный бот позволяет собрать в одном месте уведомления с различных сайтов\n"
            + "Введите /help, чтобы увидеть доступные команды";
        if (user != null) {
            message = "Вы уже зарегистрированы";
        }
        return new SendMessage(update.message().chat().id(), message);
    }

    @Override
    public String getTextOfCommand() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "зарегистрировать пользователя";
    }
}
