package edu.java.scrapper.database_test.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(properties = {
    "app.database-access-type=jdbc"})
public class JdbcLinkServiceTest extends IntegrationTest {
    private final LinkService linkService;
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;

    private final UserLinkRepository userLinkRepository;
    private final TgUserService tgUserService;
    private static final URI DEFAULT_URI = URI.create("https://stackoverflow.com/questions/42/best-way-to-allow-plugins-for-a-php-application/77#77");

    @Autowired
    public JdbcLinkServiceTest(
        LinkService linkService,
        UserRepository userRepository,
        LinkRepository linkRepository,
        UserLinkRepository userLinkRepository,
        TgUserService tgUserService
    ) {
        this.linkService = linkService;
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.userLinkRepository = userLinkRepository;
        this.tgUserService = tgUserService;
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        tgUserService.register(125L);
        tgUserService.register(12L);
        tgUserService.register(1);
        linkService.add(125L,DEFAULT_URI);
        linkService.add(12L, DEFAULT_URI);

        Optional<Link> linkOptional = linkRepository.findByURL(DEFAULT_URI);
        Assertions.assertTrue(linkOptional.isPresent());


    }

//    @Test
//    @Transactional
//    @Rollback
//    void update() {
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    void remove() {
//    }

//    @Test
//    @Transactional
//    @Rollback
//    void listAll() {
//        tgUserService.register(125L);
//        tgUserService.register(12L);
//        tgUserService.register(1);
//    }

    @Test
    @Transactional
    @Rollback
    void listAllUsers() {
        tgUserService.register(125L);
        tgUserService.register(12L);
        tgUserService.register(1);
        linkService.add(125L, DEFAULT_URI);
        linkService.add(12L, DEFAULT_URI);
        Optional<Link> linkOptional = linkRepository.findByURL(DEFAULT_URI);
        Assertions.assertTrue(linkOptional.isPresent());
        List<TgUser> userList = linkService.listAllUsers(linkOptional.get().id());
        List<TgUser> expected = List.of(new TgUser(125L, "registered"),
            new TgUser(12L, "registered"));
        Assertions.assertEquals(2, userList.size());
        Assertions.assertTrue(userList.containsAll(expected));
    }

//    @Test
//    @Transactional
//    @Rollback
//    void findOldLinks() {
//    }
}
