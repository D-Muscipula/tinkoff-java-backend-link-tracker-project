package edu.java.scheduling;

import edu.java.dto.Link;
import edu.java.service.LinkService;
import edu.java.service.jdbc.JdbcGitHubLinkUpdater;
import edu.java.service.jdbc.JdbcStackOverflowLinkUpdater;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;

@Component
public class LinkUpdaterScheduler {
    private final Logger logger = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    @Value("#{@scheduler.intervalSinceLastCheck()}")
    private Long intervalSinceLastCheck;
    private final LinkService linkService;
    private final JdbcGitHubLinkUpdater jdbcGitHubLinkUpdater;
    private final JdbcStackOverflowLinkUpdater jdbcStackOverflowLinkUpdater;

    @Autowired
    public LinkUpdaterScheduler(
        LinkService linkService,
        JdbcGitHubLinkUpdater jdbcGitHubLinkUpdater,
        JdbcStackOverflowLinkUpdater jdbcStackOverflowLinkUpdater
    ) {
        this.linkService = linkService;
        this.jdbcGitHubLinkUpdater = jdbcGitHubLinkUpdater;
        this.jdbcStackOverflowLinkUpdater = jdbcStackOverflowLinkUpdater;
    }

    @Scheduled(fixedDelayString = "#{scheduler.interval()}")
    public void update() {
        logger.info("it works");
        List<Link> oldLinks = linkService.findOldLinks(intervalSinceLastCheck);
        for (Link link : oldLinks) {
            try {
                logger.info(link.toString());
                if (link.url().getHost().equals("github.com")) {
                    jdbcGitHubLinkUpdater.update(link);
                } else if (link.url().getHost().equals("stackoverflow.com")) {
                    jdbcStackOverflowLinkUpdater.update(link);
                }
            } catch (WebClientException e) {
                logger.info("бот, вероятно,  выключен");
            }
        }
    }
}
