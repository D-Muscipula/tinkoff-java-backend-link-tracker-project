package edu.java.bot.message_handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.Start;
import edu.java.bot.commands.Track;
import edu.java.bot.commands.UnknownCommand;
import edu.java.bot.commands.UnregisteredCommand;
import edu.java.bot.commands.Untrack;
import edu.java.bot.repository.LinkRepository;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserState;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMessageHandler implements MessageHandler {
    private final LinkRepository linkRepository;
    private final List<Command> commands;
    private final Logger logger = LoggerFactory.getLogger(UserMessageHandler.class);
    private static final String UNKNOWN_MESSAGE_ANSWER = "Не могу понять, что Вы написали, попробуйте еще раз";

    public UserMessageHandler(LinkRepository linkRepository, List<Command> commands) {
        this.linkRepository = linkRepository;
        this.commands = commands;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        Message message = update.message();
        if (message == null) {
            return null;
        }

        long chatId = message.chat().id();
        User user = linkRepository.get(chatId);
        String userText = message.text();

        if (user != null) {
            logger.info(String.valueOf(user.getUserState()));
        }

        if (user != null && user.getUserState() == UserState.TRACK_STATE) {
            List<String> links = List.of(userText.split("\n"));
            user.getLinks().addAll(links);
            linkRepository.update(new User(user.getId(), UserState.REGISTERED, user.getLinks()));
        } else if (user != null && user.getUserState() == UserState.UNTRACK_STATE) {
            List<String> links = List.of(userText.split("\n"));
            user.getLinks().removeAll(links);
            linkRepository.update(new User(user.getId(), UserState.REGISTERED, user.getLinks()));

        } else if (userText.trim().charAt(0) == '/') {
            Command command = getCommand(userText, user);
            if (user == null && command instanceof Start) {
                linkRepository.add(new User(chatId, UserState.REGISTERED, new ArrayList<>()));
            } else if (command instanceof Track) {
                assert user != null;
                linkRepository.add(new User(chatId, UserState.TRACK_STATE, user.getLinks()));
            } else if (command instanceof Untrack) {
                assert user != null;
                linkRepository.add(new User(chatId, UserState.UNTRACK_STATE, user.getLinks()));
            } else if (user != null && user.getUserState() != UserState.REGISTERED) {
                linkRepository.add(new User(chatId, UserState.REGISTERED, user.getLinks()));
            }
            return command.handle(update);
        }

        return new SendMessage(chatId, UNKNOWN_MESSAGE_ANSWER);
    }

    private Command getCommand(String command, User user) {
        String trimmedCommand = command.trim();
        Command commandToReturn = new UnknownCommand();

        if (trimmedCommand.equals("/start")) {
            commandToReturn = new Start(user);
        } else if (user == null && !trimmedCommand.equals("/help")) {
            commandToReturn = new UnregisteredCommand();
        } else if (trimmedCommand.equals("/list")) {
            commandToReturn = new ListCommand(user.getLinks());
        }

        if (commandToReturn.getDescription().equals("Неизвестная команда")) {
            for (Command commandFromList : commands) {
                if (trimmedCommand.equals(commandFromList.getTextOfCommand())) {
                    return commandFromList;
                }
            }
        }

        return commandToReturn;
    }

}
