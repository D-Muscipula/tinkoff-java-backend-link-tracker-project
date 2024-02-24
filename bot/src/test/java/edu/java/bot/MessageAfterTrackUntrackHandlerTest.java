package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.message_handler.MessageAfterTrackUntrackHandler;
import edu.java.bot.message_handler.MessageHandler;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserRepositoryImpl;
import edu.java.bot.repository.UserState;
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

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
        messageHandler = new MessageAfterTrackUntrackHandler(userRepository);
    }

    @Test
    void correctLinksAfterTrackTest() {
        String userText = """
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        String expected = "Ссылки добавлены";
        userRepository.add(new User(125L, UserState.TRACK_STATE, new ArrayList<>()));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userRepository.get(id).links();
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
        userRepository.add(new User(125L, UserState.TRACK_STATE, new ArrayList<>()));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userRepository.get(id).links();
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
        userRepository.add(new User(125L, UserState.TRACK_STATE, new ArrayList<>()));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userRepository.get(id).links();
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
        userRepository.add(new User(125L, UserState.UNTRACK_STATE, links));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn(userText);
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));

        List<String> linksListFromDb = userRepository.get(id).links();
        Assertions.assertTrue(linksListFromDb.isEmpty());
    }

}
