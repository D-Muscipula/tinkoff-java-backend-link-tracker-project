package edu.java.client;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    private static final WebClient.Builder BUILDER = WebClient.builder();
    private ApplicationConfig applicationConfig;

    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public QuestionDTO getQuestion(long numberOfQuestion) {
        String baseUrl = null;
        if (applicationConfig != null) {
            baseUrl = applicationConfig.baseStackOverflowUrl();
        }
        String url = String.format("2.3/questions/%d?order=desc&sort=activity&site=stackoverflow", numberOfQuestion);
        if (baseUrl == null || baseUrl.isEmpty()) {
            //URL по умолчанию
            baseUrl = "https://api.stackexchange.com/";
        }
        return BUILDER.baseUrl(baseUrl).build()
            .get()
            .uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(QuestionDTO.class)
            .block();
    }
}
