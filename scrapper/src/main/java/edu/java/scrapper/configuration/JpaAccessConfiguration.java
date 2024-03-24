package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.repository.jpa.JpaUserRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.domain.service.jpa.JpaLinkService;
import edu.java.scrapper.domain.service.jpa.JpaTgUserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@Log4j2
public class JpaAccessConfiguration {
    @PostConstruct
    private void print() {
        log.info("jpa");
    }

    @Bean
    public TgUserService tgUserService(JpaUserRepository jpaUserRepository) {
        return new JpaTgUserService(jpaUserRepository);
    }

    @Bean
    public LinkService linkService(JpaUserRepository jpaUserRepository,
        JpaLinkRepository jpaLinkRepository
        ) {
        return new JpaLinkService(jpaUserRepository, jpaLinkRepository);
    }
}
