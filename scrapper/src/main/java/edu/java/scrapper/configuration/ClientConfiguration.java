package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.client.retry.RetryUtils;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Getter
@Configuration
public class ClientConfiguration {

    private final ApplicationConfig applicationConfig;

    private final RetryConfig retryConfig;

    @Autowired
    public ClientConfiguration(ApplicationConfig applicationConfig, RetryConfig retryConfig) {
        this.applicationConfig = applicationConfig;
        this.retryConfig = retryConfig;
    }

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient(applicationConfig, retryGithub());
    }

    @Bean
    public Retry retryGithub() {
        RetryType type = retryConfig.github().type();
        Integer maxAttempt = retryConfig.github().maxAttempt();
        Integer delay = retryConfig.github().delay();
        List<Integer> codes = retryConfig.github().codes();
        return RetryUtils.createRetry(type, maxAttempt, delay, codes);
    }

    @Bean
    public Retry retryStackoverflow() {
        RetryType type = retryConfig.stackoverflow().type();
        Integer maxAttempt = retryConfig.stackoverflow().maxAttempt();
        Integer delay = retryConfig.stackoverflow().delay();
        List<Integer> codes = retryConfig.github().codes();
        return RetryUtils.createRetry(type, maxAttempt, delay, codes);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(applicationConfig);
    }

    @Bean
    public BotClient botClient() {
        return new BotClient(applicationConfig);
    }
}
