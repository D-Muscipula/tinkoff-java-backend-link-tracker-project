package edu.java.service;

import edu.java.dto.Link;
import edu.java.dto.TgUser;
import java.net.URI;
import java.util.List;

public interface LinkService {
    void add(long tgChatId, URI url);

    void update(Link link);

    void remove(long tgChatId, URI url);

    List<Link> listAll(long tgChatId);

    List<TgUser> listAllUsers(long linkId);

    List<Link> findOldLinks(Long intervalSinceLastCheck);
}
