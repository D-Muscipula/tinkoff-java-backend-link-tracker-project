package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.client.ScrapperClient;
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
import edu.java.bot.my_bot.MyTgBot;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserRepositoryScrapperClientImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private final ApplicationConfig applicationConfig;

    public AppConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    public UserRepository dB() {
        return new UserRepositoryScrapperClientImpl(scrapperClient());
    }

    @Bean
    public MessageAfterTrackUntrackHandler messageAfterTrackUntrackHandler() {
        return new MessageAfterTrackUntrackHandler(dB());
    }

    @Bean
    public CommandMessageHandler commandMessageHandler() {
        return new CommandMessageHandler(dB(), commandList());
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
        return new UserMessageHandler(messageAfterTrackUntrackHandler(), commandMessageHandler(), dB());
    }

    @Bean
    public MyTgBot tgBot() {
        MyTgBot myTgBot = new MyTgBot(telegramBot(), messageHandler(), commandList());
        myTgBot.serve();
        return myTgBot;
    }

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient(applicationConfig);
    }
}
