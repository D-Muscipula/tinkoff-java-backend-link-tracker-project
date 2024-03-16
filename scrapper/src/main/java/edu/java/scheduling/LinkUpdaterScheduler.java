package edu.java.scheduling;

import org.springframework.scheduling.annotation.Scheduled;

public class LinkUpdaterScheduler {
//    private final Logger logger = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    @Scheduled(fixedDelayString = "#{scheduler.interval()}")
    public void update() {
//        logger.info("it works");
    }
}
