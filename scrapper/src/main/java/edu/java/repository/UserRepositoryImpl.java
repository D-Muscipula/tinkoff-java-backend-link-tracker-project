package edu.java.repository;

import edu.java.dto.TgUser;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcClient jdbcClient;

    @Autowired
    public UserRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void add(TgUser tgUser) {
        String sql = "insert into tg_user (user_chat_id, user_state)"
            + "VALUES (:id, :state)";
        this.jdbcClient.sql(sql)
                .param("id", tgUser.userChatId())
                .param("state", tgUser.userState())
                .update();
    }

    @Override
    public void remove(Long id) {
        String sql = "delete from tg_user "
            + "where user_chat_id = ?";
        this.jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Override
    public Optional<TgUser> findById(Long id) {
        String sql = "select * from tg_user"
            + " where user_chat_id = ? limit 1";
        return jdbcClient.sql(sql)
            .param(id)
            .query(TgUser.class)
            .optional();
    }

}
