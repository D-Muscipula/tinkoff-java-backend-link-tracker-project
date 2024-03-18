package edu.java.bot.repository;

import dto.response.TgUserResponse;
import edu.java.bot.client.ScrapperClient;
import java.util.List;

public class UserRepositoryScrapperClientImpl implements UserRepository {
    private final ScrapperClient scrapperClient;

    public UserRepositoryScrapperClientImpl(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public User get(Long id) {
        TgUserResponse tgUserResponse = scrapperClient.getUser(id);
        if (tgUserResponse.userState().equals("unregistered")) {
            return null;
        } else {
            List<String> links = scrapperClient.getLinks(id).links().stream()
                .map((link) -> link.url().toString()).toList();
            return new User(tgUserResponse.userChatId(), UserState.fromString(tgUserResponse.userState()),
                links
            );
        }
    }

    @Override
    public void add(User user) {

    }

    @Override
    public void update(User user) {

    }
}
