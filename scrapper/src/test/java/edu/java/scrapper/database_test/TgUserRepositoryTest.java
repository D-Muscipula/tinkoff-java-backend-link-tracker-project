package edu.java.scrapper.database_test;

import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TgUserRepositoryTest extends IntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        TgUser tgUserForAdding = new TgUser(125L, "registered");
        TgUser tgUserForAdding1 = new TgUser(12L, "registered");

        userRepository.add(tgUserForAdding);
        userRepository.add(tgUserForAdding1);

        Assertions.assertTrue(userRepository.findById(125L).isPresent());
        Assertions.assertTrue(userRepository.findById(12L).isPresent());

        TgUser foundTgUser = userRepository.findById(125L).get();
        Assertions.assertEquals(tgUserForAdding, foundTgUser);

        TgUser foundTgUser1 = userRepository.findById(12L).get();
        Assertions.assertEquals(tgUserForAdding1, foundTgUser1);

        Assertions.assertFalse(userRepository.findById(1000L).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        TgUser tgUserForAdding = new TgUser(125L, "registered");

        userRepository.add(tgUserForAdding);
        Assertions.assertTrue(userRepository.findById(125L).isPresent());

        TgUser foundTgUser = userRepository.findById(125L).get();
        Assertions.assertEquals(tgUserForAdding, foundTgUser);
        userRepository.remove(125L);

        Optional<TgUser> deletedUser = userRepository.findById(125L);
        Assertions.assertFalse(deletedUser.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void updateTest() {
        TgUser tgUserForAdding = new TgUser(125L, "registered");

        userRepository.add(tgUserForAdding);
        Assertions.assertTrue(userRepository.findById(125L).isPresent());

        TgUser foundTgUser = userRepository.findById(125L).get();
        Assertions.assertEquals(tgUserForAdding, foundTgUser);

        userRepository.updateTgUser(new TgUser(125L, "track"));

        Optional<TgUser> user = userRepository.findById(125L);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("track", user.get().userState());
    }
}
