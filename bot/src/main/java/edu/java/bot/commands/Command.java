package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

public sealed interface Command {
    SendMessage handle(Update update);

    String getTextOfCommand();

    String getDescription();

    default BotCommand toBotCommand() {
        return new BotCommand(getTextOfCommand(), getDescription());
    }

    record Start() implements Command {
        @Override
        public SendMessage handle(Update update) {
            String message = "Приветствую! Данный бот позволяет собрать в одном месте уведомления с различных сайтов\n"
                + "Введите /help, чтобы увидеть доступные команды";
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

    record Help() implements Command {
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

    record Track() implements Command {
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

    record Untrack() implements Command {
        @Override
        public SendMessage handle(Update update) {
            String message = """
                Введите сообщение с ссылкой или ссылками, которые перестанете отслеживать, в таком формате:
                -
                https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""";
            return new SendMessage(update.message().chat().id(), message).disableWebPagePreview(true);
        }

        @Override
        public String getTextOfCommand() {
            return "/untrack";
        }

        @Override
        public String getDescription() {
            return "прекратить отслеживание ссылки";
        }
    }

    record ListCommand(List<String> links) implements Command {

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

    record UnknownCommand() implements Command {
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

}
