package edu.java.scrapper.database_test.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.dto.UserLink;
import edu.java.scrapper.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.exceptions.ChatDoesntExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(properties = {
    "app.database-access-type=jdbc"})
public class JdbcTgUserServiceTest extends IntegrationTest {

    private final TgUserService tgUserService;
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;

    private final UserLinkRepository userLinkRepository;

    @Autowired
    public JdbcTgUserServiceTest(
        TgUserService tgUserService,
        UserRepository userRepository,
        LinkRepository linkRepository,
        UserLinkRepository userLinkRepository
    ) {
        this.tgUserService = tgUserService;
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.userLinkRepository = userLinkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void register() {
        tgUserService.register(125L);
        tgUserService.register(12L);
        tgUserService.register(1);
        List<TgUser> tgUsers = userRepository.findAll();
        List<TgUser> tgUserList = List.of(
            new TgUser(125L, "registered"),
            new TgUser(12L, "registered"),
            new TgUser(1L, "registered")
        );
        Assertions.assertEquals(3, tgUsers.size());
        Assertions.assertTrue(tgUsers.containsAll(tgUserList));
    }

    @Test
    @Transactional
    @Rollback
    void registerShouldThrowChatAlreadyExistsException() {
        tgUserService.register(125L);
        Assertions.assertThrows(
            ChatAlreadyExistsException.class,
            () -> tgUserService.register(125L)
        );
    }

    @Test
    @Transactional
    @Rollback
    void unregister() {
        tgUserService.register(125L);
        tgUserService.register(12L);
        tgUserService.register(1);
        List<TgUser> tgUsers = userRepository.findAll();
        List<TgUser> tgUserList = new java.util.ArrayList<>(List.of(
            new TgUser(125L, "registered"),
            new TgUser(12L, "registered"),
            new TgUser(1L, "registered")
        ));
        Assertions.assertEquals(3, tgUsers.size());
        Assertions.assertTrue(tgUsers.containsAll(tgUserList));

        tgUserService.unregister(125L);
        tgUsers = userRepository.findAll();
        tgUserList.remove(new TgUser(125L, "registered"));
        Assertions.assertEquals(2, tgUsers.size());
        Assertions.assertTrue(tgUsers.containsAll(tgUserList));

        tgUserService.unregister(12L);
        tgUserService.unregister(1L);

        tgUsers = userRepository.findAll();
        Assertions.assertTrue(tgUsers.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void unregisterShouldThrowChatDoesntExistException() {
        Assertions.assertThrows(
            ChatDoesntExistException.class,
            () -> tgUserService.unregister(125L)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Вслед за удалением пользователя должны удалиться записи в таблице users_links" +
        "а если на ссылку больше никто не подписан, то она должна удалиться из таблицы link" +
        "удаления не должны влиять на других пользователей")
    void unregisterAndCascadingDelete() {
        tgUserService.register(125L);
        tgUserService.register(12L);
        tgUserService.register(1);
        URI uriForAdd =
            URI.create("https://stackoverflow.com/questions/42/best-way-to-allow-plugins-for-a-php-application/77#77");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2024-01-31T22:22:10Z");
        OffsetDateTime lastCheckedAt = OffsetDateTime.parse("2024-02-18T16:14:29Z");

        linkRepository.add(new Link(-1L, uriForAdd, updatedAt, lastCheckedAt, null, null));

        Assertions.assertTrue(linkRepository.findByURL(uriForAdd).isPresent());
        Link foundLink = linkRepository.findByURL(uriForAdd).get();

        URI uriForAddNew = URI.create("https://stackoverflow.com/q");
        linkRepository.add(new Link(-1L, uriForAddNew, updatedAt, lastCheckedAt, null, null));

        assert linkRepository.findByURL(uriForAddNew).isPresent();
        Link newLink = linkRepository.findByURL(uriForAddNew).get();
        Long id = linkRepository.findByURL(uriForAddNew).get().id();

        //у пользователь одна ссылка
        UserLink userLink = new UserLink(-1L, 125L, foundLink.id());
        userLinkRepository.add(userLink);
        List<Link> links = userLinkRepository.findByUserId(125L).stream()
            .map((ul) -> linkRepository.findById(ul.link()).orElse(null))
            .toList();
        Assertions.assertEquals(1, links.size());
        Assertions.assertTrue(links.contains(foundLink));

        UserLink userLink1 = new UserLink(-1L, 12L, id);
        userLinkRepository.add(userLink1);

        UserLink userLink2 = new UserLink(-1L, 1L, id);
        userLinkRepository.add(userLink2);

        //у пользователя одна ссылка, и у ссылки один пользователь
        tgUserService.unregister(125L);
        Assertions.assertTrue(userRepository.findById(125L).isEmpty());
        Assertions.assertTrue(linkRepository.findById(foundLink.id()).isEmpty());
        Assertions.assertTrue(userLinkRepository.findByUserIdAndLinkId(125L, foundLink.id()).isEmpty());

        //два пользователя имеют одну ссылку
        //удаляется лишь одна запись в таблице users_links
        tgUserService.unregister(12L);
        Assertions.assertTrue(userLinkRepository.findByUserIdAndLinkId(12L, id).isEmpty());
        Assertions.assertFalse(userLinkRepository.findByUserIdAndLinkId(1L, id).isEmpty());
        Assertions.assertFalse(linkRepository.findById(id).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        tgUserService.register(125L);
        tgUserService.update(new TgUser(125L, "track"));
        TgUser tgUser = userRepository.findById(125L).orElse(new TgUser(125L, "unregistered"));
        Assertions.assertEquals("track", tgUser.userState());
    }

    @Test
    @Transactional
    @Rollback
    void updateShouldThrowChatDoesntExistException() {
        Assertions.assertThrows(
            ChatDoesntExistException.class,
            () -> tgUserService.update(new TgUser(125L, "track"))
        );
    }

    @Test
    @Transactional
    @Rollback
    void findById() {
        tgUserService.register(125L);
        tgUserService.update(new TgUser(125L, "track"));
        Assertions.assertTrue(tgUserService.findById(125L).isPresent());
        Assertions.assertEquals("track", tgUserService.findById(125L).get().userState());
    }
}
