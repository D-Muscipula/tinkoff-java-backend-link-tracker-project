package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

public record ListCommand(List<String> links) implements Command {

    @Override
    public SendMessage handle(Update update) {
        String message = "Ваши ссылки:\n" + String.join("\n", links);
        if (links.isEmpty()) {
            message = "Вы не добавили еще ни одной ссылки";
        }
        return new SendMessage(update.message().chat().id(), message).disableWebPagePreview(true);
    }

    @Override
    public String getTextOfCommand() {
        return "/list";
    }

    @Override
    public String getDescription() {
        return "показать список отслеживаемых ссылок";
    }
}
