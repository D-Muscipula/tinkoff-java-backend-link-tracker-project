package edu.java.repository;

import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.dto.UserLink;
import java.util.List;
import java.util.Optional;

public interface UserLinkRepository {
    void add(User user, Link link);

    void remove(Long id);

    Optional<UserLink> find(Long id);

    List<UserLink> findByUser(User user);

    List<UserLink> findByUser(Link link);
}
