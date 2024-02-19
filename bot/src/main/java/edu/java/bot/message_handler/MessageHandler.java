package edu.java.bot.message_handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface MessageHandler {
    SendMessage handleMessage(Update update);
}
