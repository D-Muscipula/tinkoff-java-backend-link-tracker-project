package edu.java.client;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.GitHubRepositoryDTO;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private WebClient webClient;
    private final ApplicationConfig applicationConfig;

    private static final String DEFAULT_GITHUB_URL = "https://api.github.com/repos/";

    public GitHubClient(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        setUpWebClient();
    }

    public GitHubRepositoryDTO getRepository(String user, String repository) {
        //Пример URL
        //https://api.github.com/repos/sanyarnd/java-course-2023-backend-template
        return webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(user, repository)
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GitHubRepositoryDTO.class)
            .block();
    }

    private void setUpWebClient() {
        String baseUrl = null;
        if (applicationConfig != null) {
            baseUrl = applicationConfig.baseGitHubUrl();
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = DEFAULT_GITHUB_URL;
        }
        webClient = WebClient.builder()
            .baseUrl(baseUrl).build();
    }
}
