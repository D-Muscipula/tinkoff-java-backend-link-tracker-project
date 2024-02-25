package edu.java.client;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.RepositoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private static final WebClient.Builder BUILDER = WebClient.builder();
    private ApplicationConfig applicationConfig;

    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public RepositoryDTO getRepository(String user, String repository) {
        String baseUrl = null;
        if (applicationConfig != null) {
            baseUrl = applicationConfig.baseGitHubUrl();
        }
        String url = String.format("%s/%s", user, repository);
        if (baseUrl == null || baseUrl.isEmpty()) {
            //URL по умолчанию
            baseUrl = "https://api.github.com/repos/";
        }
        return BUILDER.baseUrl(baseUrl).build()
            .get()
            .uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(RepositoryDTO.class)
            .block();
    }

    public static void main(String[] args) {
        System.out.println(new GitHubClient().getRepository("D-Muscipula", "tinkoff-java-backend-course-2023"));
    }
}
