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
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserState;
import java.util.ArrayList;
import java.util.List;

public class CommandMessageHandler implements MessageHandler {
    private final UserRepository userRepository;
    private final List<Command> commands;

    public CommandMessageHandler(UserRepository userRepository, List<Command> commands) {
        this.userRepository = userRepository;
        this.commands = commands;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        User user = userRepository.get(chatId);
        String userText = message.text();
        Command command = getCommand(userText, user);
        doUpdateInDatabase(user, command, chatId);
        return command.handle(update);
    }

    private void doUpdateInDatabase(User user, Command command, long chatId) {
        //Если пользователь не зарегистрирован и набрал /start
        if (user == null && command instanceof Start) {
            userRepository.add(new User(chatId, UserState.REGISTERED, new ArrayList<>()));
        } else if (command instanceof Track) {
            assert user != null;
            userRepository.update(new User(chatId, UserState.TRACK_STATE, user.getLinks()));
        } else if (command instanceof Untrack) {
            assert user != null;
            userRepository.update(new User(chatId, UserState.UNTRACK_STATE, user.getLinks()));
            //Если пользователь ввел до этого /track или /untrack
        } else if (user != null && user.getUserState() != UserState.REGISTERED) {
            userRepository.update(new User(chatId, UserState.REGISTERED, user.getLinks()));
        }
    }

    private Command getCommand(String command, User user) {
        String trimmedCommand = command.trim();
        Command commandToReturn = new UnknownCommand();

        if (trimmedCommand.equals("/start")) {
            commandToReturn = new Start(user);
            // Команда /help должна работать, даже если пользователь не прошел регистрацию
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
