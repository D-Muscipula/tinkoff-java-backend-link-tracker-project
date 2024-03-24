package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.dto.UserLink;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;

public class UserLinkRepositoryImpl implements UserLinkRepository {
    private final JdbcClient jdbcClient;
    private static final String SELECT_FROM_USERS_LINKS = "select id, tg_user, link from users_links ";
    private static final String DELETE_FROM = "delete from users_links ";

    public UserLinkRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void add(UserLink userLink) {
        String sql = "insert into users_links (tg_user, link)"
            + "VALUES (:user_id, :link_id)";
        this.jdbcClient.sql(sql)
            .param("user_id", userLink.tgUser())
            .param("link_id", userLink.link())
            .update();
    }

    @Override
    public void removeById(Long id) {
        String sql = DELETE_FROM
            + "where id = ?";
        this.jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Override
    public Integer removeByUserId(Long userId) {
        String sql = DELETE_FROM
            + "where tg_user = ? ";
        return this.jdbcClient.sql(sql)
            .param(userId)
            .update();
    }

    @Override
    public Integer removeByLinkId(Long userId) {
        String sql = DELETE_FROM
            + "where link = ? ";
        return this.jdbcClient.sql(sql)
            .param(userId)
            .update();
    }

    @Override
    public void removeByUserIdAndLinkId(Long userId, Long linkId) {
        String sql = DELETE_FROM
            + "where tg_user = ? and link = ? ";
        this.jdbcClient.sql(sql)
            .param(userId)
            .param(linkId)
            .update();
    }

    @Override
    public Optional<UserLink> findById(Long id) {
        String sql = SELECT_FROM_USERS_LINKS
            + "where id = ? limit 1";
        return jdbcClient.sql(sql)
            .param(id)
            .query(UserLink.class)
            .optional();
    }

    @Override
    public List<UserLink> findByUserId(Long userId) {
        String sql = SELECT_FROM_USERS_LINKS
            + "where tg_user = ?";
        return jdbcClient.sql(sql)
            .param(userId)
            .query(UserLink.class)
            .list();
    }

    @Override
    public List<UserLink> findByLinkId(Long linkId) {
        String sql = SELECT_FROM_USERS_LINKS
            + "where link = ?";
        return jdbcClient.sql(sql)
            .param(linkId)
            .query(UserLink.class)
            .list();
    }

    @Override
    public List<UserLink> findAll() {
        return jdbcClient.sql(SELECT_FROM_USERS_LINKS)
            .query(UserLink.class)
            .list();
    }

    @Override
    public Optional<UserLink> findByUserIdAndLinkId(Long userId, Long linkId) {
        String sql = SELECT_FROM_USERS_LINKS
            + "where tg_user = ? and link = ?";
        return jdbcClient.sql(sql)
            .param(userId)
            .param(linkId)
            .query(UserLink.class)
            .optional();
    }

}
