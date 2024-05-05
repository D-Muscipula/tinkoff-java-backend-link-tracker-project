package edu.java.bot.client;

import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.request.TgUserUpdate;
import dto.response.ListLinksResponse;
import dto.response.TgUserResponse;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class ScrapperClient {
    private WebClient webClient;
    private final ApplicationConfig applicationConfig;
    private final Retry retry;

    private static final String SCRAPPER_CLIENT_URL = "http://localhost:8080";

    private static final String TG_CHAT_PATH_SEGMENT = "tg-chat";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    private static final String LINKS = "links";
    private static final String STATE = "state";

    public ScrapperClient(ApplicationConfig applicationConfig, Retry retry) {
        this.applicationConfig = applicationConfig;
        this.retry = retry;
        setUpWebClient();
    }

    public void registerChat(Long tgChatId) {
        //http://localhost:8080/tg-chat/34
        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(TG_CHAT_PATH_SEGMENT, String.valueOf(tgChatId))
                .build())
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    public void deleteChat(Long tgChatId) {
        webClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(TG_CHAT_PATH_SEGMENT, String.valueOf(tgChatId))
                .build())
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient.get()
            .uri(LINKS)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry)
            .block();
    }

    public void addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        webClient.post()
            .uri(LINKS)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    public void deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        webClient.method(HttpMethod.DELETE)
            .uri(LINKS)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    public void updateUser(TgUserUpdate tgUserUpdate) {
        webClient.method(HttpMethod.POST)
            .uri(STATE)
            .bodyValue(tgUserUpdate)
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    public TgUserResponse getUser(Long tgChatId) {
        return webClient.method(HttpMethod.GET)
            .uri(STATE)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .bodyToMono(TgUserResponse.class)
            .retryWhen(retry)
            .block();
    }

    private void setUpWebClient() {
        String baseUrl = null;
        if (applicationConfig != null) {
            baseUrl = applicationConfig.baseScrapperClientUrl();
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = SCRAPPER_CLIENT_URL;
        }
        webClient = WebClient.builder()
            .baseUrl(baseUrl).build();
    }
}
