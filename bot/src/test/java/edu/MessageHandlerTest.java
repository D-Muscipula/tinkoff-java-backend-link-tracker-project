package edu;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.db_plug.Db;
import edu.java.bot.db_plug.DataBase;
import edu.java.bot.message_handler.MessageHandler;
import edu.java.bot.message_handler.UserMessageHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {
    private MessageHandler messageHandler;
    @Mock
    Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @BeforeEach
    void setUp() {
        DataBase dataBase = new Db();
        List<Command> commandList = new ArrayList<>() {
            {
                add(new Command.Start());
                add(new Command.Help());
                add(new Command.Track());
                add(new Command.Untrack());
                add(new Command.ListCommand(new ArrayList<>()));
            }
        };
        messageHandler = new UserMessageHandler(dataBase, commandList);
    }

    @Test
    void handleMessageTest() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/list");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals("Вы не добавили еще ни одной ссылки", map.get("text"));

    }

    @Test
    void getStartCommandTest() {
        Assertions.assertInstanceOf(Command.Start.class, messageHandler.getCommand("/start"));
        Assertions.assertInstanceOf(Command.Start.class, messageHandler.getCommand("/start "));
        Assertions.assertInstanceOf(Command.Start.class, messageHandler.getCommand(" /start"));
    }

    @Test
    void getHelpCommandTest() {
        Assertions.assertInstanceOf(Command.Help.class, messageHandler.getCommand("/help"));
        Assertions.assertInstanceOf(Command.Help.class, messageHandler.getCommand("/help "));
        Assertions.assertInstanceOf(Command.Help.class, messageHandler.getCommand(" /help "));
    }

    @Test
    void getTrackCommandTest() {
        Assertions.assertInstanceOf(Command.Track.class, messageHandler.getCommand("/track"));
        Assertions.assertInstanceOf(Command.Track.class, messageHandler.getCommand("/track "));
        Assertions.assertInstanceOf(Command.Track.class, messageHandler.getCommand(" /track"));
    }

    @Test
    void getUntrackCommandTest() {
        Assertions.assertInstanceOf(Command.Untrack.class, messageHandler.getCommand("/untrack"));
        Assertions.assertInstanceOf(Command.Untrack.class, messageHandler.getCommand("/untrack "));
        Assertions.assertInstanceOf(Command.Untrack.class, messageHandler.getCommand(" /untrack"));
    }

    @Test
    void getListCommandTest() {
        Assertions.assertInstanceOf(Command.ListCommand.class, messageHandler.getCommand("/list"));
        Assertions.assertInstanceOf(Command.ListCommand.class, messageHandler.getCommand("/list "));
        Assertions.assertInstanceOf(Command.ListCommand.class, messageHandler.getCommand(" /list"));
    }

    @Test
    void getUnknownCommandTest() {
        Assertions.assertInstanceOf(Command.UnknownCommand.class, messageHandler.getCommand("/aboba"));
        Assertions.assertInstanceOf(Command.UnknownCommand.class, messageHandler.getCommand("/aboba "));
        Assertions.assertInstanceOf(Command.UnknownCommand.class, messageHandler.getCommand(" /aboba"));
    }
}
