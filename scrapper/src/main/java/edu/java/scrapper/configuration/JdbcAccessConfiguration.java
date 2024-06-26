package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.domain.repository.jdbc.LinkRepositoryImpl;
import edu.java.scrapper.domain.repository.jdbc.UserLinkRepositoryImpl;
import edu.java.scrapper.domain.repository.jdbc.UserRepositoryImpl;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgUserService;
import edu.java.scrapper.domain.service.jdbc.JdbcLinkService;
import edu.java.scrapper.domain.service.jdbc.JdbcTgUserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@Log4j2
public class JdbcAccessConfiguration {
    private final JdbcClient jdbcClient;

    @PostConstruct
    private void print() {
        log.info("jdbc");
    }

    @Autowired
    public JdbcAccessConfiguration(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl(jdbcClient);
    }

    @Bean
    public LinkRepository linkRepository() {
        return new LinkRepositoryImpl(jdbcClient);
    }

    @Bean
    public UserLinkRepository userLinkRepository() {
        return new UserLinkRepositoryImpl(jdbcClient);
    }

    @Bean
    public TgUserService tgUserService() {
        return new JdbcTgUserService(userRepository(), linkRepository(), userLinkRepository());
    }

    @Bean
    public LinkService linkService() {
        return new JdbcLinkService(userRepository(), linkRepository(), userLinkRepository());
    }
}
