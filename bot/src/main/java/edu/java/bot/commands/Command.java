package edu.java.bot.commands;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Collectors;

public sealed interface Command {
    SendMessage handle(Update update);

    record Start() implements Command {
        @Override
        public SendMessage handle(Update update) {
            return new SendMessage(update.message().chat().id(), "Привет");
        }
    }

    record Help() implements Command {
        @Override
        public SendMessage handle(Update update) {
            String message = """
                /start -- зарегистрировать пользователя
                /help -- вывести окно с командами
                /track -- начать отслеживание ссылки
                /untrack -- прекратить отслеживание ссылки
                /list -- показать список отслеживаемых ссылок
                    """;
            return new SendMessage(update.message().chat().id(), message);
        }
    }

    record Track() implements Command {
        @Override
        public SendMessage handle(Update update) {
            return new SendMessage(update.message().chat().id(), "Введите ссылку");
        }
    }

    record Untrack() implements Command {
        @Override
        public SendMessage handle(Update update) {
            return new SendMessage(update.message().chat().id(), "Введите ссылку, которую перестанете отслеживать");
        }
    }

    record ListCommand(List<String> links) implements Command {

        @Override
        public SendMessage handle(Update update) {
            String message = "Ваши ссылки:\n" + String.join("\n", links);
            if (links.isEmpty()) {
                message = "Вы не добавили еще ни одной ссылки";
            }
            return new SendMessage(update.message().chat().id(), message);
        }
    }

    record UnknownCommand() implements Command {
        @Override
        public SendMessage handle(Update update) {
            return new SendMessage(update.message().chat().id(), "Неизвестная комманда");
        }
    }

}
