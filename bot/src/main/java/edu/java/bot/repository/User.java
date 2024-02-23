package edu.java.bot.repository;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
    private final Long id;
    @Setter
    private UserState userState;
    private final List<String> links;

    public User(Long id, UserState userState, List<String> links) {
        this.id = id;
        this.userState = userState;
        this.links = links;
    }

    public void addLinks(String... linksToAdd) {
        links.addAll(Arrays.asList(linksToAdd));
    }

    public void removeLinks(String... linksToRemove) {
        links.removeAll(List.of(linksToRemove));
    }

}
