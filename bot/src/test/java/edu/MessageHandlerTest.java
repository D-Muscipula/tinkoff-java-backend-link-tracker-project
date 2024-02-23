package edu;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.Help;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.Start;
import edu.java.bot.commands.Track;
import edu.java.bot.commands.Untrack;
import edu.java.bot.repository.UserRepositoryImpl;
import edu.java.bot.repository.UserRepository;
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
        UserRepository userRepository = new UserRepositoryImpl();
        List<Command> commandList = new ArrayList<>() {
            {
                add(new Start(null));
                add(new Help());
                add(new Track());
                add(new Untrack());
                add(new ListCommand(new ArrayList<>()));
            }
        };
        //messageHandler = new UserMessageHandler(userRepository, commandList);
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

    /*@Test
    void getStartCommandTest() {
        Assertions.assertInstanceOf(Start.class, messageHandler.getCommand("/start"));
        Assertions.assertInstanceOf(Start.class, messageHandler.getCommand("/start "));
        Assertions.assertInstanceOf(Start.class, messageHandler.getCommand(" /start"));
    }

    @Test
    void getHelpCommandTest() {
        Assertions.assertInstanceOf(Help.class, messageHandler.getCommand("/help"));
        Assertions.assertInstanceOf(Help.class, messageHandler.getCommand("/help "));
        Assertions.assertInstanceOf(Help.class, messageHandler.getCommand(" /help "));
    }

    @Test
    void getTrackCommandTest() {
        Assertions.assertInstanceOf(Track.class, messageHandler.getCommand("/track"));
        Assertions.assertInstanceOf(Track.class, messageHandler.getCommand("/track "));
        Assertions.assertInstanceOf(Track.class, messageHandler.getCommand(" /track"));
    }

    @Test
    void getUntrackCommandTest() {
        Assertions.assertInstanceOf(Untrack.class, messageHandler.getCommand("/untrack"));
        Assertions.assertInstanceOf(Untrack.class, messageHandler.getCommand("/untrack "));
        Assertions.assertInstanceOf(Untrack.class, messageHandler.getCommand(" /untrack"));
    }

    @Test
    void getListCommandTest() {
        Assertions.assertInstanceOf(ListCommand.class, messageHandler.getCommand("/list"));
        Assertions.assertInstanceOf(ListCommand.class, messageHandler.getCommand("/list "));
        Assertions.assertInstanceOf(ListCommand.class, messageHandler.getCommand(" /list"));
    }

    @Test
    void getUnknownCommandTest() {
        Assertions.assertInstanceOf(UnknownCommand.class, messageHandler.getCommand("/aboba"));
        Assertions.assertInstanceOf(UnknownCommand.class, messageHandler.getCommand("/aboba "));
        Assertions.assertInstanceOf(UnknownCommand.class, messageHandler.getCommand(" /aboba"));
    }*/
}
