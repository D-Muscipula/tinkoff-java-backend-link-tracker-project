package edu.java.scrapper.client_test;

import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.client.GitHubClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.GitHubRepositoryDTO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
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
public class GitHubClientTest extends AbstractClientTest {
    @Mock
    private ApplicationConfig applicationConfig;

    @Override
    @BeforeEach
    void init() {
        Mockito.when(applicationConfig.baseGitHubUrl()).thenReturn("http://localhost:8080/");
    }

    @Test
    public void getRepositoryTest() throws IOException {
        String body =
            FileUtils.readFileToString(new File("src/test/resources/github.json"), StandardCharsets.UTF_8);

        stubFor(WireMock.get(urlEqualTo("/abc/abc"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(body)));

        GitHubClient gitHubClient = new GitHubClient(applicationConfig);
        GitHubRepositoryDTO repository = gitHubClient.getRepository("abc", "abc");

        Assertions.assertEquals(751082162, repository.id());
        Assertions.assertEquals("sanyarnd/java-course-2023-backend-template", repository.fullName());
        OffsetDateTime createdAt = OffsetDateTime.parse("2024-01-31T22:22:10Z");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2024-02-18T16:14:29Z");
        OffsetDateTime pushedAt = OffsetDateTime.parse("2024-02-25T15:51:14Z");
        Assertions.assertEquals(createdAt, repository.createdAt());
        Assertions.assertEquals(updatedAt, repository.updatedAt());
        Assertions.assertEquals(pushedAt, repository.pushedAt());
    }

}
