package edu.java.client;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.StackOverflowQuestionDTO;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    private WebClient webClient;
    private final ApplicationConfig applicationConfig;
    private static final String DEFAULT_STACKOVERFLOW_URL = "https://api.github.com/repos/";

    public StackOverflowClient(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        setUpWebClient();
    }

    public StackOverflowQuestionDTO getQuestion(long numberOfQuestion) {
        //Пример URL
        //https://api.stackexchange.com/2.3/questions/34?order=desc&sort=activity&site=stackoverflow
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("2.3", "questions", String.valueOf(numberOfQuestion))
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("site", "stackoverflow")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(StackOverflowQuestionDTO.class)
            .block();
    }

    private void setUpWebClient() {
        String baseUrl = null;
        if (applicationConfig != null) {
            baseUrl = applicationConfig.baseStackOverflowUrl();
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = DEFAULT_STACKOVERFLOW_URL;
        }
        webClient = WebClient.builder()
            .baseUrl(baseUrl).build();
    }
}
