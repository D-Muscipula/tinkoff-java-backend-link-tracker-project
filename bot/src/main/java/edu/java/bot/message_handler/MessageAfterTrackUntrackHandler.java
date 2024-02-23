package edu.java.bot.message_handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserState;
import java.net.URI;
import java.net.URISyntaxException;
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
        int countOfLinks = user.getLinks().size();
        botAnswer = new StringBuilder("Ссылки добавлены");
        for (var link : links) {
            if (link != null && validator.isValid(link)) {
                try {
                    URI uri = new URI(link);
                    String host = uri.getHost();
                    //пока так
                    if (!host.equals("github.com") && !host.equals("stackoverflow.com")) {
                        botAnswer.append("\n").append(host).append(" не поддерживается");
                    }
                } catch (URISyntaxException e) {
                    botAnswer.append("\n" + "Ссылка ").append(link).append(" " + "некорректна");
                }
                user.getLinks().add(link);
            } else {
                botAnswer.append("\nСсылка ").append(link).append(" некорректна");
            }
        }
        //Если нет корректных ссылок, то "Ссылки добавлены" не выводится
        if (user.getLinks().size() == countOfLinks) {
            botAnswer.delete(0, "Ссылки добавлены\n".length());
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
