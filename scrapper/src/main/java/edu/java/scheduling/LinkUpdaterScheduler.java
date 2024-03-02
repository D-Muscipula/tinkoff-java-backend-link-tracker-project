package edu.java.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class LinkUpdaterScheduler {
    private final Logger logger = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    @Scheduled(fixedDelayString = "#{clientConfiguration.applicationConfig.scheduler().interval()}")
    public void update() {
        logger.info("it works");
    }
}
