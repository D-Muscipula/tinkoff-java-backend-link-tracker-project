package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry", ignoreUnknownFields = false)
public record RetryConfig(
    @Bean
    @NotNull
    Github github,
    @Bean
    @NotNull
    Stackoverflow stackoverflow,
    @Bean
    @NotNull
    Bot bot
) {
    public record Github(RetryType type, Integer maxAttempt, Integer delay, List<Integer> codes) {
    }

    public record Stackoverflow(RetryType type, Integer maxAttempt, Integer delay, List<Integer> codes) {
    }

    public record Bot(RetryType type, Integer maxAttempt, Integer delay, List<Integer> codes) {
    }
}
