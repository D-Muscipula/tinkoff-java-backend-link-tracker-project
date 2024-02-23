package edu.java.bot;

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
import edu.java.bot.message_handler.CommandMessageHandler;
import edu.java.bot.message_handler.MessageAfterTrackUntrackHandler;
import edu.java.bot.message_handler.MessageHandler;
import edu.java.bot.message_handler.UserMessageHandler;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import edu.java.bot.repository.UserState;
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

    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
        List<Command> commandList = new ArrayList<>() {
            {
                add(new Start(null));
                add(new Help());
                add(new Track());
                add(new Untrack());
                add(new ListCommand(new ArrayList<>()));
            }
        };
        MessageAfterTrackUntrackHandler messageAfterTrackUntrackHandler = new MessageAfterTrackUntrackHandler(userRepository);
        CommandMessageHandler commandMessageHandler = new CommandMessageHandler(userRepository, commandList);
        messageHandler = new UserMessageHandler(messageAfterTrackUntrackHandler, commandMessageHandler, userRepository);
    }

    @Test
    void startCommandWithUnregisteredUser() {
        String text = "Приветствую! Данный бот позволяет собрать в одном месте уведомления с различных сайтов\n"
            + "Введите /help, чтобы увидеть доступные команды";
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/start");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));
    }

    @Test
    void startCommandWithRegisteredUser() {
        userRepository.add(new User(125L, UserState.REGISTERED, new ArrayList<>()));
        String text = "Вы уже зарегистрированы";
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/start");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));
    }

    @Test
    void helpCommandWithUnregisteredUser() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        String text = """
            /start - зарегистрировать пользователя
            /help - вывести окно с командами
            /track - начать отслеживание ссылки
            /untrack - прекратить отслеживание ссылки
            /list - показать список отслеживаемых ссылок""";
        ;
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/help");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));

    }

    @Test
    void trackCommandWithRegisteredUser() {
        userRepository.add(new User(125L, UserState.REGISTERED, new ArrayList<>()));
        String text = """
            Введите сообщение с ссылкой или ссылками для отслеживания в таком формате:
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/track");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));
    }

    @Test
    void untrackCommandWithRegisteredUser() {
        userRepository.add(new User(125L, UserState.REGISTERED, new ArrayList<>()));
        String text = """
            Введите сообщение с ссылкой или ссылками, которые перестанете отслеживать, в таком формате:
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/untrack");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));
    }

    @Test
    void listCommandWithRegisteredUserAndEmptyList() {
        userRepository.add(new User(125L, UserState.REGISTERED, new ArrayList<>()));
        String text = "Вы не добавили еще ни одной ссылки";
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/list");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));
    }

    @Test
    void listCommandWithRegisteredUserAndNotEmptyList() {
        List<String> list = new ArrayList<>() {{
            add("https://github.com/sanyarnd/tinkoff-java-course-2023/");
            add("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        }};
        userRepository.add(new User(125L, UserState.REGISTERED, list));
        String text = """
            Ваши ссылки:
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c""";
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/list");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));
    }

    @Test
    void unknownCommand() {
        userRepository.add(new User(125L, UserState.REGISTERED, new ArrayList<>()));
        String text = "Неизвестная команда";
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/abobus");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals(text, map.get("text"));
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

        List<String> linksListFromDb = userRepository.get(id).getLinks();
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

        List<String> linksListFromDb = userRepository.get(id).getLinks();
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

        List<String> linksListFromDb = userRepository.get(id).getLinks();
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

        List<String> linksListFromDb = userRepository.get(id).getLinks();
        Assertions.assertTrue(linksListFromDb.isEmpty());
    }


}
