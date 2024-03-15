package edu.java.repository;

import edu.java.dto.Link;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepositoryImpl implements LinkRepository {
    private final JdbcClient jdbcClient;

    @Autowired
    public LinkRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void add(Link link) {

    }

    @Override
    public void remove(Link link) {

    }

    @Override
    public Optional<Link> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Link> findByURL(Long id) {
        return Optional.empty();
    }
}
