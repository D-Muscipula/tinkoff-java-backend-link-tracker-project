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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    private long id;

    @BeforeEach
    void setUp() {
        id = 125L;
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(id);
    }

//    @Test
//    void StartTest() {
//        Command command = new Start();
//        SendMessage sendMessage = command.handle(update);
//        Map<String, Object> map = sendMessage.getParameters();
//        Assertions.assertEquals(id, map.get("chat_id"));
//        Assertions.assertNotEquals("", map.get("text"));
//    }

    @Test
    void HelpTest() {
        Command command = new Help();
        SendMessage sendMessage = command.handle(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertNotEquals("", map.get("text"));
    }

    @Test
    void TrackTest() {
        Command command = new Track();
        SendMessage sendMessage = command.handle(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertNotEquals("", map.get("text"));
    }

    @Test
    void UntrackTest() {
        Command command = new Untrack();
        SendMessage sendMessage = command.handle(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertNotEquals("", map.get("text"));
    }

    //Пустой лист
    @Test
    void EmptyListCommandTest() {
        Command command = new ListCommand(new ArrayList<>());
        SendMessage sendMessage = command.handle(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals("Вы не добавили еще ни одной ссылки", map.get("text"));
    }

    //Есть ссылки
    @Test
    void ListCommandTest() {
        List<String> links = new ArrayList<>() {{
            add("https://github.com/sanyarnd/tinkoff-java-course-2023/");
            add("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        }};
        String expected = """
            Ваши ссылки:
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c""";
        Command command = new ListCommand(links);
        SendMessage sendMessage = command.handle(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(expected, map.get("text"));
    }
}
