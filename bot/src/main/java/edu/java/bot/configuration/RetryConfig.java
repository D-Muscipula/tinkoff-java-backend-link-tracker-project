package edu.java.bot.configuration;

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
    ScrapperRetry scrapperRetry
) {
    public record ScrapperRetry(RetryType type, Integer maxAttempt, Integer delay, List<Integer> codes) {
    }
}
