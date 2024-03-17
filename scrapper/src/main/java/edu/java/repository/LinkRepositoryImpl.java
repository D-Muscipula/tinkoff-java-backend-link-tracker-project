package edu.java.repository;

import edu.java.dto.Link;
import java.net.URI;
import java.util.List;
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
        String sql = "insert into link (url, updated_at, last_checked_at)"
            + "VALUES (:url, :updated_at, :last_checked_at)";
        this.jdbcClient.sql(sql)
            .param("url", link.url().toString())
            .param("updated_at", link.updatedAt())
            .param("last_checked_at", link.lastCheckedAt())
            .update();
    }

    @Override
    public void update(Link link) {
        String sql = "update link "
            + "set updated_at = ?, last_checked_at = ?, "
            + "last_commit_sha = ?, answers_count = ? "
            + " where id = ?";
        this.jdbcClient.sql(sql)
            .param(link.updatedAt())
            .param(link.lastCheckedAt())
            .param(link.lastCommitSha())
            .param(link.answersCount())
            .param(link.id())
            .update();
    }

    @Override
    public void removeById(Long id) {
        String sql = "delete from link "
            + "where id = ?";
        this.jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Override
    public void removeByURL(URI url) {
        String sql = "delete from link"
            + " where url = ?";
        this.jdbcClient.sql(sql)
            .param(url.toString())
            .update();
    }

    @Override
    public Optional<Link> findById(Long id) {
        String sql = "select * from link "
            + "where id = ? limit 1";
        return jdbcClient.sql(sql)
            .param(id)
            .query(Link.class)
            .optional();
    }

    @Override
    public Optional<Link> findByURL(URI url) {
        String sql = "select * from link"
            + " where url = ? limit 1";
        return jdbcClient.sql(sql)
            .param(url.toString())
            .query(Link.class)
            .optional();
    }

    @Override
    public List<Link> findAll() {
        String sql = " select * from link";
        return jdbcClient.sql(sql)
            .query(Link.class)
            .list();
    }
}
