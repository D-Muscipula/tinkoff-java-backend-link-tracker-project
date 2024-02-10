package edu.java.bot.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Commands {
    START("/start"), HELP("/help"), TRACK("/track"), UNTRACK("/untrack"), LIST("/list");
    @Getter(AccessLevel.PUBLIC)
    private final String title;
    ///start -- зарегистрировать пользователя
///help -- вывести окно с командами
///track -- начать отслеживание ссылки
///untrack -- прекратить отслеживание ссылки
///list -- показать список отслеживаемых ссылок
}
