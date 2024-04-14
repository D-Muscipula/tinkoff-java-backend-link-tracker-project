package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.retry.RetryUtils;
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
import edu.java.bot.service.UserScrapperService;
import edu.java.bot.service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
public class AppConfig {
    private final ApplicationConfig applicationConfig;
    private final RetryConfig retryConfig;
    private final MeterRegistry meterRegistry;

    @Autowired
    public AppConfig(ApplicationConfig applicationConfig, RetryConfig retryConfig, MeterRegistry meterRegistry) {
        this.applicationConfig = applicationConfig;
        this.retryConfig = retryConfig;
        this.meterRegistry = meterRegistry;
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    public UserService dB() {
        return new UserScrapperService(scrapperClient());
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
    public MessageHandler messageHandler(MeterRegistry meterRegistry) {
        return new UserMessageHandler(messageAfterTrackUntrackHandler(),
            commandMessageHandler(), dB(), meterRegistry);
    }

    @Bean
    public MyTgBot tgBot() {
        MyTgBot myTgBot = new MyTgBot(telegramBot(), messageHandler(meterRegistry), commandList());
        myTgBot.serve();
        return myTgBot;
    }

    @Bean
    public Retry retryScrapper() {
        RetryType type = retryConfig.scrapperRetry().type();
        Integer maxAttempt = retryConfig.scrapperRetry().maxAttempt();
        Integer delay = retryConfig.scrapperRetry().delay();
        List<Integer> codes = retryConfig.scrapperRetry().codes();
        return RetryUtils.createRetry(type, maxAttempt, delay, codes);
    }

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient(applicationConfig, retryScrapper());
    }
}
