package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.DB;
import edu.java.bot.DataBase;
import edu.java.bot.MessageHandler;
import edu.java.bot.MyTgBot;
import edu.java.bot.UserMessageHandler;
import jakarta.validation.constraints.NotEmpty;
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
    public DataBase DB() {
        return new DB();
    }

    @Bean
    public MessageHandler messageHandler() {
        return new UserMessageHandler(DB());
    }
    @Bean
    public MyTgBot tgBot() {
        MyTgBot myTgBot = new MyTgBot(telegramBot(), messageHandler());
        myTgBot.serve();
        return myTgBot;
    }

}
