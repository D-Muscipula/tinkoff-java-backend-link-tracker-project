package edu.java.scrapper.client_test;

import com.github.tomakehurst.wiremock.client.WireMock;
import dto.request.LinkUpdate;
import edu.java.client.BotClient;
import edu.java.configuration.ApplicationConfig;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@ExtendWith(MockitoExtension.class)
public class BotClientTest extends AbstractClientTest {
    @Mock
    private ApplicationConfig applicationConfig;

    @Override
    @BeforeEach
    void init() {
        Mockito.when(applicationConfig.baseBotClientUrl()).thenReturn("http://localhost:8080/");
    }

    @SneakyThrows @Test
    public void sendUpdateTest() throws IOException {

        stubFor(WireMock.post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")));

        BotClient botClient = new BotClient(applicationConfig);
        URI uri = new URI("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        LinkUpdate linkUpdate = new LinkUpdate(10L,
            uri, "some link", List.of(1L, 30L, 10L)
        );
        botClient.sendUpdate(linkUpdate);

        String json = "{\"id\":10," +
            "\"url\":\"https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c\"," +
            "\"description\":\"some link\"," +
            "\"tgChatIds\":[1,30,10]}";
        verify(postRequestedFor(urlEqualTo("/updates"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(containing(json)));
    }

    @Test
    public void sendUpdateWithErrorCodeTest() throws IOException {

        stubFor(WireMock.post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")));

        BotClient botClient = new BotClient(applicationConfig);
        URI uri = null;
        try {
            uri = new URI("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LinkUpdate linkUpdate = new LinkUpdate(10L,
            uri, "some link", List.of(1L, 30L, 10L)
        );

        Assertions.assertThrows(WebClientResponseException.BadRequest.class, () -> botClient.sendUpdate(linkUpdate));

    }

}
