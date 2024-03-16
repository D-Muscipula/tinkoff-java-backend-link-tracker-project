package edu.java.scrapper.database_test;

import edu.java.dto.User;
import edu.java.repository.UserRepository;
import edu.java.repository.UserRepositoryImpl;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@SpringBootTest
public class UserRepositoryTest extends IntegrationTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    void addTest() {
//        User userForAdding = new User(125L, "registered");
//
//        UserRepository userRepository = new UserRepositoryImpl(jdbcClient);
//        userRepository.add(userForAdding);
//        Assertions.assertTrue(userRepository.findById(125L).isPresent());
//
//        User foundUser = userRepository.findById(125L).get();
//        Assertions.assertEquals(userForAdding, foundUser);
//        userRepository.remove(125L);
//
//        Optional<User> deletedUser = userRepository.findById(125L);
//        Assertions.assertFalse(deletedUser.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
    }
}
