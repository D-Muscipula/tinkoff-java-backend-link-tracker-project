package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JooqUserLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JooqUserRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.domain.service.jooq.JooqLinkService;
import edu.java.scrapper.domain.service.jooq.JooqTgUserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@Log4j2
public class JooqAccessConfiguration {
    private final DSLContext dslContext;

    public JooqAccessConfiguration(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @PostConstruct
    private void print() {
        log.info("jooq");
    }

    @Bean
    public UserRepository userRepository() {
        return new JooqUserRepository(dslContext);
    }

    @Bean
    public LinkRepository linkRepository() {
        return new JooqLinkRepository(dslContext);
    }

    @Bean
    public UserLinkRepository userLinkRepository() {
        return new JooqUserLinkRepository(dslContext);
    }

    @Bean
    public TgUserService tgUserService() {
        return new JooqTgUserService(userRepository());
    }

    @Bean
    public LinkService linkService() {
        return new JooqLinkService(userRepository(), linkRepository(), userLinkRepository());
    }
}
