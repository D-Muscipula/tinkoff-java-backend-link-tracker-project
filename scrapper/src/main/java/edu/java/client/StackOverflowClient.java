package edu.java.client;

import edu.java.dto.QuestionDTO;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {

    QuestionDTO getQuestion(long numberOfQuestion) {
        String url = String.format("2.3/questions/%d?order=desc&sort=activity&site=stackoverflow", numberOfQuestion);

        WebClient.Builder builder = WebClient.builder().baseUrl("https://api.stackexchange.com/");
        return builder.build()
            .get()
            .uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(QuestionDTO.class)
            .block();
    }

    @SuppressWarnings("checkstyle:UncommentedMain")
    public static void main(String[] args) {
        StackOverflowClient stackOverflowClient = new StackOverflowClient();
        QuestionDTO questionDTO = stackOverflowClient.getQuestion(34L);
        System.out.println(questionDTO);
    }
}
