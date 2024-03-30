package edu.java.bot.client_test;

import com.github.tomakehurst.wiremock.client.WireMock;
import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import dto.response.TgUserResponse;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.ApplicationConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@ExtendWith(MockitoExtension.class)
public class ScrapperClientTest extends AbstractClientTest {
    @Mock
    private ApplicationConfig applicationConfig;
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @BeforeEach
    @Override
    void init() {
        Mockito.when(applicationConfig.baseScrapperClientUrl()).thenReturn("http://localhost:8080/");
    }

    @Test
    public void registerChatTest() {

        stubFor(WireMock.post(urlEqualTo("/tg-chat/125"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")));

        ScrapperClient scrapperClient = new ScrapperClient(applicationConfig);
        scrapperClient.registerChat(125L);

        verify(postRequestedFor(urlEqualTo("/tg-chat/125")));
    }

    @Test
    public void deleteChatTest() {

        stubFor(WireMock.delete(urlEqualTo("/tg-chat/125"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")));

        ScrapperClient scrapperClient = new ScrapperClient(applicationConfig);
        scrapperClient.deleteChat(125L);

        verify(deleteRequestedFor(urlEqualTo("/tg-chat/125")));
    }

    @SneakyThrows
    @Test
    public void getLinksTest() {
        String body = """
            {
              "links": [
                {
                  "id": 1,
                  "url": "https://example.com/1"
                },
                {
                  "id": 2,
                  "url": "https://example.com/2"
                },
                {
                  "id": 3,
                  "url": "https://example.com/3"
                }
              ],
              "size": 3
            }""";

        stubFor(WireMock.get(urlEqualTo("/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(body)));

        ScrapperClient scrapperClient = new ScrapperClient(applicationConfig);
        ListLinksResponse listLinksResponse = scrapperClient.getLinks(125L);
        Assertions.assertEquals(3, listLinksResponse.size());
        List<LinkResponse> links = List.of(
            new LinkResponse(1L, new URI("https://example.com/1")),
            new LinkResponse(2L, new URI("https://example.com/2")),
            new LinkResponse(3L, new URI("https://example.com/3"))
        );
        Assertions.assertEquals(3, listLinksResponse.size());
        Assertions.assertEquals(links, listLinksResponse.links());
        verify(getRequestedFor(urlEqualTo("/links"))
            .withHeader(TG_CHAT_ID_HEADER, containing("125")));
    }

    @SneakyThrows
    @Test
    public void addLinkTest() {
        String body = """
            {"link":"https://example.com/1"}""";

        stubFor(WireMock.post(urlEqualTo("/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")));

        ScrapperClient scrapperClient = new ScrapperClient(applicationConfig);
        scrapperClient.addLink(125L, new AddLinkRequest(new URI("https://example.com/1")));
        verify(postRequestedFor(urlEqualTo("/links"))
            .withRequestBody(containing(body)));
    }

    @SneakyThrows
    @Test
    public void deleteLinkTest() {
        String body = """
            {"link":"https://example.com/1"}""";

        stubFor(WireMock.delete(urlEqualTo("/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")));

        ScrapperClient scrapperClient = new ScrapperClient(applicationConfig);
        scrapperClient.deleteLink(125L, new RemoveLinkRequest(new URI("https://example.com/1")));
        verify(deleteRequestedFor(urlEqualTo("/links"))
            .withRequestBody(containing(body)));
    }


    @SneakyThrows
    @Test
    public void getStateTest() {
        String body = """
            {
            "userChatId": 123,
            "userState": "track"
            }
            """;

        stubFor(WireMock.get(urlEqualTo("/state"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(body)
                .withHeader("Content-Type", "application/json")));

        ScrapperClient scrapperClient = new ScrapperClient(applicationConfig);
        TgUserResponse a = scrapperClient.getUser(123L);
        Assertions.assertEquals("track", a.userState());
        verify(getRequestedFor(urlEqualTo("/state")));
    }

}
