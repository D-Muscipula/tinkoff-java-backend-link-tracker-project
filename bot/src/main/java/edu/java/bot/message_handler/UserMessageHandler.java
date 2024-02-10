package edu.java.bot.message_handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.db_plug.DataBase;
import java.util.List;

public class UserMessageHandler implements MessageHandler {
    private final DataBase dataBase;
    private final List<Command> commands;

    public UserMessageHandler(DataBase dataBase, List<Command> commands) {
        this.dataBase = dataBase;
        this.commands = commands;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        Message message = update.message();
        if (message == null) {
            return null;
        }

        long chatId = message.chat().id();
        String userText = message.text();
        String botText = "";

        if (userText.trim().charAt(0) == '/') {
            Command command = getCommand(userText);
            return command.handle(update);
        }

        return new SendMessage(chatId, botText);
    }

    public Command getCommand(String command) {
        String trimmedCommand = command.trim();

        if (trimmedCommand.equals("/list")) {
            return new Command.ListCommand(dataBase.getLinks());
        }
        for (Command commandFromList : commands) {
            if (trimmedCommand.equals(commandFromList.getTextOfCommand())) {
                return commandFromList;
            }
        }
        return new Command.UnknownCommand();
    }

}
