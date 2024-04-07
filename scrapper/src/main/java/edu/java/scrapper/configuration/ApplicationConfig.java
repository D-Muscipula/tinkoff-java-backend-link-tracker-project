package edu.java.scrapper.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@OpenAPIDefinition(info = @Info(title = "Scrapper API",
                                description = "Scrapper API", version = "1.0.0"))
public record ApplicationConfig(
    @NotNull
    @Bean
    Scheduler scheduler,
    String baseGitHubUrl,
    String baseStackOverflowUrl,
    String baseBotClientUrl,
    AccessType databaseAccessType,

    String kafkaTopicName
) {

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay,
                            @NotNull Duration intervalSinceLastCheck) {
    }
}
