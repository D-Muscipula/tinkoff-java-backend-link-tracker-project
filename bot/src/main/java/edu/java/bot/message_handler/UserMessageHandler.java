package edu.java.bot.message_handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserState;
import edu.java.bot.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMessageHandler implements MessageHandler {
    private final UserService userService;
    private final MessageAfterTrackUntrackHandler messageAfterTrackUntrackHandler;
    private final CommandMessageHandler commandMessageHandler;
    private final Counter processMessageCounter;
    private final Logger logger = LoggerFactory.getLogger(UserMessageHandler.class);
    public static final String UNKNOWN_MESSAGE_ANSWER = "Не могу понять, что Вы написали, попробуйте еще раз";

    public UserMessageHandler(
        MessageAfterTrackUntrackHandler messageAfterTrackUntrackHandler,
        CommandMessageHandler commandMessageHandler, UserService userService, MeterRegistry meterRegistry
    ) {
        this.userService = userService;
        this.messageAfterTrackUntrackHandler = messageAfterTrackUntrackHandler;
        this.commandMessageHandler = commandMessageHandler;
        processMessageCounter = meterRegistry.counter("number_of_processed_messages");
    }

    @Override
    public SendMessage handleMessage(Update update) {
        processMessageCounter.increment();
        Message message = update.message();
        if (message == null || message.text() == null) {
            return null;
        }

        long chatId = message.chat().id();
        User user = userService.get(chatId);
        String userText = message.text();
        logger.info("Текст " + userText);
        if (user != null) {
            logger.info("User: " + user.userState());
        } else {
            logger.info("Пользователь еще не зарегистирован");
        }
        //Прошлая команда была /track или /untrack, и бот ожидает ссылки
        if (user != null
            && (user.userState() == UserState.TRACK_STATE
            || user.userState() == UserState.UNTRACK_STATE)) {
            return messageAfterTrackUntrackHandler.handleMessage(update);

            //Если введена команда
        } else if (userText.trim().charAt(0) == '/') {
            return commandMessageHandler.handleMessage(update);
        }

        return new SendMessage(chatId, UNKNOWN_MESSAGE_ANSWER);
    }

}
