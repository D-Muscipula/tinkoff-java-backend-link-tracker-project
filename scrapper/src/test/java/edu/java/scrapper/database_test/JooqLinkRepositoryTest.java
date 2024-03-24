package edu.java.scrapper.database_test;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@TestPropertySource(locations="classpath:test.properties")
public class JooqLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Добавление и поиск по url и id")
    void addFindTest() {
        URI uriForAdd = URI.create("https://stackoverflow.com/questions/42/best-way-to-allow-plugins-for-a-php-application/77#77");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2024-01-31T22:22:10Z");
        OffsetDateTime lastCheckedAt = OffsetDateTime.parse("2024-02-18T16:14:29Z");

        linkRepository.add(new Link(-1L, uriForAdd, updatedAt, lastCheckedAt, null, null));
        Assertions.assertTrue(linkRepository.findByURL(uriForAdd).isPresent());

        Link foundLink = linkRepository.findByURL(uriForAdd).get();

        Assertions.assertEquals(uriForAdd, foundLink.url());

        Assertions.assertEquals(0, ChronoUnit.SECONDS.between(updatedAt, foundLink.updatedAt()));
        Assertions.assertEquals(0, ChronoUnit.SECONDS.between(lastCheckedAt, foundLink.lastCheckedAt()));

        URI uriForAddNew = URI.create("https://stackoverflow.com/q");
        linkRepository.add(new Link(-1L, uriForAddNew , updatedAt, lastCheckedAt, null, null));

        Long id = linkRepository.findByURL(uriForAddNew).get().id();
        Assertions.assertTrue(linkRepository.findById(id).isPresent());

    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаление по url и id")
    void removeTest() {
        URI uriForAdd = URI.create("https://stackoverflow.com/questions/42/best-way-to-allow-plugins-for-a-php-application/77#77");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2024-01-31T22:22:10Z");
        OffsetDateTime lastCheckedAt = OffsetDateTime.parse("2024-02-18T16:14:29Z");

        linkRepository.add(new Link(-1L, uriForAdd, updatedAt, lastCheckedAt, null, null));
        Assertions.assertTrue(linkRepository.findByURL(uriForAdd).isPresent());

        linkRepository.removeByURL(uriForAdd);
        Optional<Link> deletedLink= linkRepository.findByURL(uriForAdd);
        Assertions.assertFalse(deletedLink.isPresent());

        linkRepository.add(new Link(-1L, uriForAdd, updatedAt, lastCheckedAt, null, null));
        Long id = linkRepository.findByURL(uriForAdd).get().id();

        linkRepository.removeById(id);
        Optional<Link> deletedLink1 = linkRepository.findById(id);
        Assertions.assertFalse(deletedLink1.isPresent());
    }
}

