package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.message_handler.MessageAfterTrackUntrackHandler;
import edu.java.bot.message_handler.MessageHandler;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepositoryImpl;
import edu.java.bot.repository.UserState;
import edu.java.bot.service.UserService;
import edu.java.bot.service.UserServiceImpl;
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
public class MessageAfterTrackUntrackHandlerTest {
    private MessageHandler messageHandler;
    @Mock
    Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(new UserRepositoryImpl());
        messageHandler = new MessageAfterTrackUntrackHandler(userService);
    }

    @Test
    void correctLinksAfterTrackTest() {
        String userText = """
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        String expected = "Ссылки добавлены";
        userService.add(new User(125L, UserState.TRACK_STATE, new ArrayList<>()));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userService.get(id).links();
        List<String> links = new ArrayList<>(){{
            add("https://github.com/sanyarnd/tinkoff-java-course-2023/");
            add("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
            add("https://stackoverflow.com/search?q=unsupported%20link");
        }};
        Assertions.assertTrue(linksListFromDb.containsAll(links));
    }

    @Test
    void incorrectLinksAfterTrackTest() {
        String userText = """
            https://abobus/abobus/abobus/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        String expected = "Ссылки добавлены\n"
            + "Ссылка https://abobus/abobus/abobus/ некорректна";
        userService.add(new User(125L, UserState.TRACK_STATE, new ArrayList<>()));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userService.get(id).links();
        List<String> links = new ArrayList<>(){{
            add("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
            add("https://stackoverflow.com/search?q=unsupported%20link");
        }};
        Assertions.assertTrue(linksListFromDb.containsAll(links));
    }

    @Test
    void inappropriateLinksAfterTrackTest() {
        String userText = """
            https://www.youtube.com/watch?v=dQw4w9WgXcQ
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        String expected = "Ссылки добавлены\n"
            + "www.youtube.com не поддерживается";
        userService.add(new User(125L, UserState.TRACK_STATE, new ArrayList<>()));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userService.get(id).links();
        List<String> links = new ArrayList<>(){{
            add("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
            add("https://stackoverflow.com/search?q=unsupported%20link");
        }};
        Assertions.assertTrue(linksListFromDb.containsAll(links));
    }

    @Test
    void unTrackTest() {
        String userText = """
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        List<String> links = new ArrayList<>(){{
            add("https://github.com/sanyarnd/tinkoff-java-course-2023/");
            add("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
            add("https://stackoverflow.com/search?q=unsupported%20link");
        }};
        String expected = "Ссылки удалены";
        userService.add(new User(125L, UserState.UNTRACK_STATE, links));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userService.get(id).links();
        Assertions.assertTrue(linksListFromDb.isEmpty());
    }

}
