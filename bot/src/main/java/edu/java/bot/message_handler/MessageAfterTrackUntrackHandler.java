package edu.java.bot.message_handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserState;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.validator.routines.UrlValidator;
import org.jetbrains.annotations.NotNull;

public class MessageAfterTrackUntrackHandler implements MessageHandler {
    private final UserRepository userRepository;
    private final UrlValidator validator = new UrlValidator();

    public MessageAfterTrackUntrackHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        User user = userRepository.get(chatId);
        String userText = message.text();
        if (user.getUserState() == UserState.TRACK_STATE) {
            return new SendMessage(chatId, handleMessageAfterTrack(userText, user));
        } else {
            return new SendMessage(chatId, handleMessageAfterUntrack(userText, user));
        }
    }

    @NotNull
    private String handleMessageAfterTrack(String userText, User user) {
        StringBuilder botAnswer;
        List<String> links = List.of(userText.split("\n"));
        botAnswer = new StringBuilder("Ссылки добавлены");
        for (var link : links) {
            if (link != null && validator.isValid(link)) {
                try {
                    URL url = Paths.get(link).toUri().toURL();
                    String host = url.getHost();
                    //пока так
                    if (!host.equals("github.com") && !host.equals("stackoverflow.com")) {
                        botAnswer.append(host).append(" не поддерживается");
                    }
                } catch (MalformedURLException e) {
                    botAnswer.append("\n" + "Ссылка ").append(link).append(" " + "некорректна");
                }
                user.getLinks().add(link);
            } else {
                botAnswer.append("\nСсылка ").append(link).append(" некорректна");
            }
        }
        userRepository.update(new User(user.getId(), UserState.REGISTERED, user.getLinks()));
        return botAnswer.toString();
    }

    @NotNull
    private String handleMessageAfterUntrack(String userText, User user) {
        String botAnswer;
        List<String> links = List.of(userText.split("\n"));
        user.getLinks().removeAll(links);
        userRepository.update(new User(user.getId(), UserState.REGISTERED, user.getLinks()));
        botAnswer = "Ссылки удалены";
        return botAnswer;
    }
}
