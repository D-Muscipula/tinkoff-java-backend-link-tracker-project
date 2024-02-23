package edu.java.bot.message_handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMessageHandler implements MessageHandler {
    private final UserRepository userRepository;
    private final MessageAfterTrackUntrackHandler messageAfterTrackUntrackHandler;
    private final CommandMessageHandler commandMessageHandler;
    private final Logger logger = LoggerFactory.getLogger(UserMessageHandler.class);
    private static final String UNKNOWN_MESSAGE_ANSWER = "Не могу понять, что Вы написали, попробуйте еще раз";

    public UserMessageHandler(
        UserRepository userRepository,
        MessageAfterTrackUntrackHandler messageAfterTrackUntrackHandler, CommandMessageHandler commandMessageHandler
        ) {
        this.userRepository = userRepository;
        this.messageAfterTrackUntrackHandler = messageAfterTrackUntrackHandler;
        this.commandMessageHandler = commandMessageHandler;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        Message message = update.message();
        if (message == null) {
            return null;
        }

        long chatId = message.chat().id();
        User user = userRepository.get(chatId);
        String userText = message.text();

        if (user != null) {
            logger.info(String.valueOf(user.getUserState()));
        }
        //Прошлая команда была /track или /untrack и бот ожидает ссылки
        if (user != null
            && (user.getUserState() == UserState.TRACK_STATE
            || user.getUserState() == UserState.UNTRACK_STATE)) {
            return messageAfterTrackUntrackHandler.handleMessage(update);
            //Если введена команда
        } else if (userText.trim().charAt(0) == '/') {
            return commandMessageHandler.handleMessage(update);
        }

        return new SendMessage(chatId, UNKNOWN_MESSAGE_ANSWER);
    }

}
