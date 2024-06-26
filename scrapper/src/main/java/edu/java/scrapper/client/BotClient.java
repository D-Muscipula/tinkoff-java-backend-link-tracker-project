package edu.java.scrapper.client;

import dto.request.LinkUpdate;
import edu.java.scrapper.configuration.ApplicationConfig;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class BotClient {
    private WebClient webClient;
    private final ApplicationConfig applicationConfig;
    private final Retry retry;

    private static final String BOT_CLIENT_URL = "http://localhost:8090";

    public BotClient(ApplicationConfig applicationConfig, Retry retry) {
        this.applicationConfig = applicationConfig;
        this.retry = retry;
        setUpWebClient();
    }

    public void sendUpdate(LinkUpdate linkUpdate) {
        //http://localhost:8090/updates
        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("updates")
                .build())
            .bodyValue(linkUpdate)
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    private void setUpWebClient() {
        String baseUrl = null;
        if (applicationConfig != null) {
            baseUrl = applicationConfig.baseBotClientUrl();
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = BOT_CLIENT_URL;
        }
        webClient = WebClient.builder()
            .baseUrl(baseUrl).build();
    }
}
