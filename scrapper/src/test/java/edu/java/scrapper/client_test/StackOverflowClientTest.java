package edu.java.scrapper.client_test;

import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.client.StackOverflowClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.StackOverflowQuestionDTO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import edu.java.scrapper.client_test.AbstractClientTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@ExtendWith(MockitoExtension.class)
public class StackOverflowClientTest extends AbstractClientTest {
    @Mock
    private ApplicationConfig applicationConfig;

    @Override
    @BeforeEach
    void init() {
        Mockito.when(applicationConfig.baseStackOverflowUrl()).thenReturn("http://localhost:8080/");
    }

    @Test
    public void getQuestionTest() throws IOException {
        String body =
            FileUtils.readFileToString(new File("src/test/resources/stackoverflow.json"), StandardCharsets.UTF_8);

        stubFor(WireMock.get(urlEqualTo("/2.3/questions/34?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(body)));

        StackOverflowClient stackOverflowClient = new StackOverflowClient(applicationConfig);
        StackOverflowQuestionDTO question = stackOverflowClient.getQuestion(34L);

        Assertions.assertEquals(34, question.items().getFirst().questionId());
        Assertions.assertEquals("How to unload a ByteArray using Actionscript 3?", question.items().getFirst().title());
        OffsetDateTime creationDate = OffsetDateTime.parse("2008-08-01T12:30:57Z");
        OffsetDateTime lastActivityDate = OffsetDateTime.parse("2022-11-21T06:37:32Z");
        OffsetDateTime lastEditDate = OffsetDateTime.parse("2020-12-02T17:21:52Z");

        Assertions.assertEquals(creationDate, question.items().getFirst().creationDate());
        Assertions.assertEquals(lastActivityDate, question.items().getFirst().lastActivityDate());
        Assertions.assertEquals(lastEditDate, question.items().getFirst().lastEditDate());
    }
}
