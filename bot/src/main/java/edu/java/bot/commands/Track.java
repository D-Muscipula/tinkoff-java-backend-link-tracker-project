package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public record Track() implements Command {
    @Override
    public SendMessage handle(Update update) {
        String message = """
            Введите сообщение с ссылкой или ссылками для отслеживания в таком формате:
            +
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        return new SendMessage(update.message().chat().id(), message).disableWebPagePreview(true);
    }

    @Override
    public String getTextOfCommand() {
        return "/track";
    }

    @Override
    public String getDescription() {
        return "начать отслеживание ссылки";
    }
}
