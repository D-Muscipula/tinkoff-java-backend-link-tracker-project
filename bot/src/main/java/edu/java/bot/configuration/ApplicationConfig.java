package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.commands.Command;
import edu.java.bot.db_plug.DB;
import edu.java.bot.db_plug.DataBase;
import edu.java.bot.message_handler.MessageHandler;
import edu.java.bot.MyTgBot;
import edu.java.bot.message_handler.UserMessageHandler;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import java.util.ArrayList;
import java.util.List;

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
    public List<Command> commandList() {
        return new ArrayList<>() {{
            add(new Command.Start());
            add(new Command.Help());
            add(new Command.Track());
            add(new Command.Untrack());
            add(new Command.ListCommand(new ArrayList<>()));
        }};
    }

    @Bean
    public MessageHandler messageHandler() {
        return new UserMessageHandler(DB(), commandList());
    }
    @Bean
    public MyTgBot tgBot() {
        MyTgBot myTgBot = new MyTgBot(telegramBot(), messageHandler(), commandList());
        myTgBot.serve();
        return myTgBot;
    }

}
