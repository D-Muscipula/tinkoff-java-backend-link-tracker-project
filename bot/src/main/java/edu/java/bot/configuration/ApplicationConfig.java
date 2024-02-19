package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.MyTgBot;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.Help;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.Start;
import edu.java.bot.commands.Track;
import edu.java.bot.commands.Untrack;
import edu.java.bot.repository.LinkRepository;
import edu.java.bot.repository.LinkRepositoryImpl;
import edu.java.bot.message_handler.MessageHandler;
import edu.java.bot.message_handler.UserMessageHandler;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken
) {
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }

    @Bean
    public LinkRepository dB() {
        return new LinkRepositoryImpl();
    }

    @Bean
    public List<Command> commandList() {
        return new ArrayList<>() {{
            add(new Start(null));
            add(new Help());
            add(new Track());
            add(new Untrack());
            add(new ListCommand(new ArrayList<>()));
        }};
    }

    @Bean
    public MessageHandler messageHandler() {
        return new UserMessageHandler(dB(), commandList());
    }

    @Bean
    public MyTgBot tgBot() {
        MyTgBot myTgBot = new MyTgBot(telegramBot(), messageHandler(), commandList());
        myTgBot.serve();
        return myTgBot;
    }

}
