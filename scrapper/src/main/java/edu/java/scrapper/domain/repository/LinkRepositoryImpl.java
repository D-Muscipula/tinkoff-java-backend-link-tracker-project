package edu.java.scrapper.domain.repository;

import edu.java.scrapper.dto.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;

public class LinkRepositoryImpl implements LinkRepository {
    private final JdbcClient jdbcClient;
    private final static String SELECT_FROM_LINK =
        "select id, url, updated_at, last_checked_at, last_commit_sha, answers_count from link ";
    private final static String DELETE_FROM_LINK = "delete from link ";

    public LinkRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void add(Link link) {
        String sql = "insert into link (url, updated_at, last_checked_at, last_commit_sha, answers_count)"
            + "VALUES (:url, :updated_at, :last_checked_at, :last_commit_sha, :answers_count)";
        this.jdbcClient.sql(sql)
            .param("url", link.url().toString())
            .param("updated_at", link.updatedAt())
            .param("last_checked_at", link.lastCheckedAt())
            .param("last_commit_sha", link.lastCommitSha())
            .param("answers_count", link.answersCount())
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
        String sql = DELETE_FROM_LINK
            + "where id = ?";
        this.jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Override
    public void removeByURL(URI url) {
        String sql = DELETE_FROM_LINK
            + " where url = ?";
        this.jdbcClient.sql(sql)
            .param(url.toString())
            .update();
    }

    @Override
    public Optional<Link> findById(Long id) {
        String sql = SELECT_FROM_LINK
            + "where id = ? limit 1";
        return jdbcClient.sql(sql)
            .param(id)
            .query(Link.class)
            .optional();
    }

    @Override
    public Optional<Link> findByURL(URI url) {
        String sql = SELECT_FROM_LINK
            + "where url = ? limit 1";
        return jdbcClient.sql(sql)
            .param(url.toString())
            .query(Link.class)
            .optional();
    }

    @Override
    public List<Link> findAll() {
        return jdbcClient.sql(SELECT_FROM_LINK)
            .query(Link.class)
            .list();
    }
}
