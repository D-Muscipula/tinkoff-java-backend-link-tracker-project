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
public class CommandMessageHandlerTest {

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
        messageHandler = new CommandMessageHandler(userRepository, commandList);
    }

    @Test
    void someCommandWithUnregisteredUser() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        long id = 125L;
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(message.text()).thenReturn("/list");
        SendMessage sendMessage = messageHandler.handleMessage(update);
        Map<String, Object> map = sendMessage.getParameters();
        Assertions.assertEquals(id, map.get("chat_id"));
        Assertions.assertEquals("Вы не зарегистрированы, наберите /start для регистрации", map.get("text"));

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
}
