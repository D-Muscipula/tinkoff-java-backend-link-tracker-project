package edu.java.scrapper.database_test.jdbc;

import edu.java.scrapper.IntegrationEnvironmentTest;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "app.database-access-type=jdbc"})
public class JdbcLinkServiceTest extends IntegrationTest {
    private final LinkService linkService;
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;

    private final UserLinkRepository userLinkRepository;

    public JdbcLinkServiceTest(
        LinkService linkService,
        UserRepository userRepository,
        LinkRepository linkRepository,
        UserLinkRepository userLinkRepository
    ) {
        this.linkService = linkService;
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.userLinkRepository = userLinkRepository;
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void remove() {
    }

    @Test
    void listAll() {
    }

    @Test
    void listAllUsers() {
    }

    @Test
    void findOldLinks() {
    }
}
