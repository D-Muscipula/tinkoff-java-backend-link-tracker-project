package edu.java.scrapper.database_test;

import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.dto.UserLink;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TgUserLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private UserLinkRepository userLinkRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private UserRepository userRepository;

    private static Link defaultLinkForAdding;

    private static TgUser defaultTgUser;
    private static OffsetDateTime updatedAt;
    private static OffsetDateTime lastCheckedAt;
    private static URI uriForAdd;

    @BeforeAll
    static void init() {
        uriForAdd =
            URI.create("https://stackoverflow.com/questions/42/best-way-to-allow-plugins-for-a-php-application/77#77");
        updatedAt = OffsetDateTime.parse("2024-01-31T22:22:10Z");
        lastCheckedAt = OffsetDateTime.parse("2024-02-18T16:14:29Z");
        defaultLinkForAdding = new Link(1L, uriForAdd, updatedAt, lastCheckedAt, null, null);
        defaultTgUser = new TgUser(10L, "registered");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Поиск по id пользователя и ссылки")
    void findByUserAndLinkId() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, defaultTgUser.userChatId(), linkId);
        userLinkRepository.add(userLink);

        Assertions.assertTrue(userLinkRepository.findByUserIdAndLinkId(userId, linkId).isPresent());
        UserLink foundUserLink = userLinkRepository.findByUserIdAndLinkId(userId, linkId).get();

        Assertions.assertEquals(foundUserLink.tgUser(), userId);
        Assertions.assertEquals(foundUserLink.link(), linkId);
        Assertions.assertNotEquals(-1L, foundUserLink.id());
    }


    @Test
    @Transactional
    @Rollback
    @DisplayName("Добавление и поиск по id")
    void addFindByIdTest() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, userId, linkId);
        userLinkRepository.add(userLink);

        Long id = userLinkRepository.findByUserIdAndLinkId(userId, linkId).get().id();
        Assertions.assertTrue(userLinkRepository.findById(id).isPresent());
        UserLink foundUserLink = userLinkRepository.findById(id).get();
        Assertions.assertEquals(foundUserLink.tgUser(), defaultTgUser.userChatId());
        Assertions.assertEquals(foundUserLink.link(), linkId);

        TgUser tgUserForAdding1 = new TgUser(11L, "registered");
        Link linkForAdding1 = new Link(-1L, URI.create("abc"), updatedAt, lastCheckedAt, null, null);

        userRepository.add(tgUserForAdding1);
        linkRepository.add(linkForAdding1);
        Long userId1 = tgUserForAdding1.userChatId();
        Long linkId1 = linkRepository.findByURL(URI.create("abc")).get().id();
        UserLink userLink1 = new UserLink(-1L, userId1, linkId1);

        userLinkRepository.add(userLink1);

        Long id1 = userLinkRepository.findByUserIdAndLinkId(userId1, linkId1).get().id();
        Assertions.assertTrue(userLinkRepository.findById(id1).isPresent());
        UserLink foundUserLink1 = userLinkRepository.findById(id1).get();
        Assertions.assertEquals(foundUserLink1.tgUser(), userId1);
        Assertions.assertEquals(foundUserLink1.link(), linkId1);
    }


    @Test
    @Transactional
    @Rollback
    @DisplayName("Поиск по id пользователя")
    void findByUserIdTest() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, userId, linkId);
        userLinkRepository.add(userLink);

        Link linkForAdding1 = new Link(-1L, URI.create("abc"), updatedAt, lastCheckedAt, null, null);
        linkRepository.add(linkForAdding1);
        Long linkId1 = linkRepository.findByURL(URI.create("abc")).get().id();
        UserLink userLink1 = new UserLink(-1L, userId, linkId1);
        userLinkRepository.add(userLink1);

        List<UserLink> userLinkList = userLinkRepository.findByUserId(userId);
        Assertions.assertEquals(2, userLinkList.size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Поиск по id ссылки")
    void findByLinkIdTest() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, userId, linkId);
        userLinkRepository.add(userLink);

        TgUser tgUserForAdding1 = new TgUser(11L, "registered");
        UserLink userLink1 = new UserLink(-1L, tgUserForAdding1.userChatId(), linkId);

        userRepository.add(tgUserForAdding1);
        userLinkRepository.add(userLink1);

        List<UserLink> userLinkList = userLinkRepository.findByLinkId(linkId);
        Assertions.assertEquals(2, userLinkList.size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаление по id ")
    void removeByIdTest() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, userId, linkId);
        userLinkRepository.add(userLink);

        Assertions.assertTrue(userLinkRepository.findByUserIdAndLinkId(userId, linkId).isPresent());
        Long userLinkId = userLinkRepository.findByUserIdAndLinkId(userId, linkId).get().id();

        userLinkRepository.removeById(userLinkId);
        Assertions.assertFalse(userLinkRepository.findByUserIdAndLinkId(userId, linkId).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаление по id пользователя и ссылки")
    void removeByUserAndLinkIdTest() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, defaultTgUser.userChatId(), linkId);
        userLinkRepository.add(userLink);

        Assertions.assertTrue(userLinkRepository.findByUserIdAndLinkId(userId, linkId).isPresent());

        userLinkRepository.removeByUserIdAndLinkId(userId, linkId);
        Assertions.assertFalse(userLinkRepository.findByUserIdAndLinkId(userId, linkId).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаление по id пользователя")
    void removeByUserIdTest() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, defaultTgUser.userChatId(), linkId);
        userLinkRepository.add(userLink);

        Link linkForAdding1 = new Link(-1L, URI.create("abc"), updatedAt, lastCheckedAt, null, null);
        linkRepository.add(linkForAdding1);
        Long linkId1 = linkRepository.findByURL(URI.create("abc")).get().id();
        UserLink userLink1 = new UserLink(-1L, defaultTgUser.userChatId(), linkId1);

        userLinkRepository.add(userLink1);

        Long userId1 = defaultTgUser.userChatId();
        Integer countOfDeleted = userLinkRepository.removeByUserId(userId1);
        Assertions.assertEquals(2, countOfDeleted);

        Assertions.assertEquals(0, userLinkRepository.findByUserId(userId).size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаление по id ссылки")
    void removeByLinkIdTest() {
        userRepository.add(defaultTgUser);
        linkRepository.add(defaultLinkForAdding);
        Long userId = defaultTgUser.userChatId();
        Long linkId = linkRepository.findByURL(uriForAdd).get().id();
        UserLink userLink = new UserLink(-1L, userId, linkId);
        userLinkRepository.add(userLink);

        TgUser tgUserForAdding1 = new TgUser(11L, "registered");
        UserLink userLink1 = new UserLink(-1L, tgUserForAdding1.userChatId(), linkId);

        userRepository.add(tgUserForAdding1);
        userLinkRepository.add(userLink1);

        Integer countOfDeleted = userLinkRepository.removeByLinkId(linkId);
        Assertions.assertEquals(2, countOfDeleted);

        Assertions.assertEquals(0, userLinkRepository.findByLinkId(linkId).size());
    }
}
