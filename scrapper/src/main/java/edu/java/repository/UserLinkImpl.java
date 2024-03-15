package edu.java.repository;

import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.dto.UserLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class UserLinkImpl implements UserLinkRepository {
    private final JdbcClient jdbcClient;

    @Autowired
    public UserLinkImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void add(User user, Link link) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Optional<UserLink> find(Long id) {
        return Optional.empty();
    }

    @Override
    public List<UserLink> findByUser(User user) {
        return null;
    }

    @Override
    public List<UserLink> findByUser(Link link) {
        return null;
    }
}
