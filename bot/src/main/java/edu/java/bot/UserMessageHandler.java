package edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.Commands;
import java.util.ArrayList;
import java.util.List;

public class UserMessageHandler implements MessageHandler{
    private DataBase dataBase;

    public UserMessageHandler(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        Message message = update.message();
        if (message == null) {
            return null;
        }
        long chatId = message.chat().id();
        String userText = message.text();
        String botText = userText + ", " + message.from().firstName();
        if (userText.charAt(0) == '/') {
            Command command = getCommand(userText);
            return command.handle(update);
        }


        return new SendMessage(chatId, botText);
    }

    public Command getCommand(String command) {
        if(command.equals(Commands.START.getTitle())) {
            return new Command.Start();
        } else if (command.equals(Commands.HELP.getTitle())) {
            return new Command.Help();
        } else if (command.equals(Commands.TRACK.getTitle())) {
            return new Command.Track();
        } else if (command.equals(Commands.UNTRACK.getTitle())) {
            return new Command.Untrack();
        } else if (command.equals(Commands.LIST.getTitle())) {
            return new Command.ListCommand(getLinksFromDB());
        }
        return new Command.UnknownCommand();
    }

    private List<String> getLinksFromDB() {
        return new ArrayList<>(){{
            add("dsfsdf");
            add("dsafdsafds");
        }};
    }

}
