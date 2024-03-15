package edu.java.repository;

import edu.java.dto.User;
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
    public void add(User user) {
        String sql = "insert into users (user_chat_id, user_state)"
            + "VALUES (:id, :state)";
        this.jdbcClient.sql(sql)
                .param("id", user.userChatId())
                .param("state", user.userState())
                .update();
    }

    @Override
    public void remove(Long id) {
        String sql = "delete from users "
            + "where user_chat_id = ?";
        this.jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select * from users"
            + " where user_chat_id = ? limit 1";
        return jdbcClient.sql(sql)
            .param(id)
            .query(User.class)
            .optional();
    }

}
