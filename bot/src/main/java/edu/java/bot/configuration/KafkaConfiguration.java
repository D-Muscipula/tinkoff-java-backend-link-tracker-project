package edu.java.bot.configuration;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Log4j2
public class KafkaConfiguration {
    private final ApplicationConfig applicationConfig;

    @Autowired
    public KafkaConfiguration(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic(applicationConfig.kafkaTopicName(), 1, (short) 1);
    }

    @KafkaListener(id = "myId", topics = "updates")
    public void listen(String in) {
        log.info(in);
    }
}
