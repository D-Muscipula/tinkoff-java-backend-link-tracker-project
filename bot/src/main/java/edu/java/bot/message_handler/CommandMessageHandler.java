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
import edu.java.bot.repository.UserState;
import edu.java.bot.service.UserService;
import java.util.ArrayList;
import java.util.List;

public class CommandMessageHandler implements MessageHandler {
    private final UserService userService;
    private final List<Command> commands;

    public CommandMessageHandler(UserService userService, List<Command> commands) {
        this.userService = userService;
        this.commands = commands;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        User user = userService.get(chatId);
        String userText = message.text();
        Command command = getCommand(userText, user);
        doUpdateInDatabase(user, command, chatId);
        return command.handle(update);
    }

    private Command getCommand(String command, User user) {
        String trimmedCommand = command.trim();
        Command commandToReturn = new UnknownCommand();

        if (trimmedCommand.equals("/start")) {
            commandToReturn = new Start(user);
            // Команда /help должна работать, даже если пользователь не прошел регистрацию
            // Если какая-то другая команда при незарегистрированном пользователе,
            // то выводится сообщение о регистрации
        } else if (user == null && !trimmedCommand.equals("/help")) {
            commandToReturn = new UnregisteredCommand();
        } else if (trimmedCommand.equals("/list")) {
            commandToReturn = new ListCommand(user.links());
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

    private void doUpdateInDatabase(User user, Command command, long chatId) {
        //Если пользователь не зарегистрирован и набрал /start
        if (user == null && command instanceof Start) {
            userService.add(new User(chatId, UserState.REGISTERED, new ArrayList<>()));
        } else if (command instanceof Track) {
            assert user != null;
            userService.update(new User(chatId, UserState.TRACK_STATE, user.links()));
        } else if (command instanceof Untrack) {
            assert user != null;
            userService.update(new User(chatId, UserState.UNTRACK_STATE, user.links()));
            //Если пользователь ввел до этого /track или /untrack
        } else if (user != null && user.userState() != UserState.REGISTERED) {
            userService.update(new User(chatId, UserState.REGISTERED, user.links()));
        }
    }
}
