package edu.java.scrapper.scheduling;

import edu.java.scrapper.dto.Link;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.jdbc.JdbcGitHubLinkUpdater;
import edu.java.scrapper.service.jdbc.JdbcStackOverflowLinkUpdater;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdaterScheduler {
    private final Logger logger = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    @Value("#{@scheduler.intervalSinceLastCheck()}")
    private Long intervalSinceLastCheck;
    private final LinkService linkService;
    private final Map<String, LinkUpdater> updaters = new HashMap<>();

    @Autowired
    public LinkUpdaterScheduler(
        LinkService linkService,
        JdbcGitHubLinkUpdater jdbcGitHubLinkUpdater,
        JdbcStackOverflowLinkUpdater jdbcStackOverflowLinkUpdater
    ) {
        this.linkService = linkService;
        updaters.put(jdbcGitHubLinkUpdater.getHost(), jdbcGitHubLinkUpdater);
        updaters.put(jdbcStackOverflowLinkUpdater.getHost(), jdbcStackOverflowLinkUpdater);
    }

    @Scheduled(fixedDelayString = "#{scheduler.interval()}")
    public void update() {
        logger.info("it works");
        List<Link> oldLinks = linkService.findOldLinks(intervalSinceLastCheck);
        for (Link link : oldLinks) {
            logger.info(link.toString());
            LinkUpdater linkUpdater = updaters.get(link.url().getHost());
            if (linkUpdater != null) {
                logger.info(linkUpdater.getHost() + " update");
                linkUpdater.update(link);
            }
        }
    }
}
