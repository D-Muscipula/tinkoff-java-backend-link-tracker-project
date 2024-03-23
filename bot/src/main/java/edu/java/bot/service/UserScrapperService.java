package edu.java.bot.service;

import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.request.TgUserUpdate;
import dto.response.TgUserResponse;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserState;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class UserScrapperService implements UserService {
    private final ScrapperClient scrapperClient;

    public UserScrapperService(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public User get(Long id) {
        TgUserResponse tgUserResponse = scrapperClient.getUser(id);
        if (tgUserResponse.userState().equals("unregistered")) {
            return null;
        } else {
            List<String> links = new ArrayList<>(scrapperClient.getLinks(id).links().stream()
                .map((link) -> link.url().toString()).toList());
            return new User(tgUserResponse.userChatId(), UserState.fromString(tgUserResponse.userState()),
                links
            );
        }
    }

    @Override
    public void add(User user) {
        scrapperClient.registerChat(user.id());
    }

    @Override
    public void update(User user) {
        TgUserResponse tgUserResponse = scrapperClient.getUser(user.id());
        UserState currentUserState = UserState.fromString(tgUserResponse.userState());
        UserState futureUserState = user.userState();
        if (currentUserState == UserState.REGISTERED
            && futureUserState == UserState.TRACK_STATE || futureUserState == UserState.UNTRACK_STATE) {
            scrapperClient.updateUser(new TgUserUpdate(user.id(), futureUserState.getValue()));
        } else if (currentUserState == UserState.TRACK_STATE && futureUserState == UserState.REGISTERED) {
            for (int i = 0; i < user.links().size(); i++) {
                scrapperClient.addLink(
                    user.id(),
                    new AddLinkRequest(URI.create(user.links().get(i)))
                );
                scrapperClient.updateUser(new TgUserUpdate(user.id(), futureUserState.getValue()));
            }
        } else if (currentUserState == UserState.UNTRACK_STATE && futureUserState == UserState.REGISTERED) {
            List<String> list = new ArrayList<>(scrapperClient.getLinks(user.id()).links().stream()
                    .map((link) -> link.url().toString()).toList());
            list.removeAll(user.links());
            for (String string : list) {
                scrapperClient.deleteLink(
                        user.id(),
                        new RemoveLinkRequest(URI.create(string))
                );
                scrapperClient.updateUser(new TgUserUpdate(user.id(), futureUserState.getValue()));
            }
        }

    }
}
