package edu.java.bot.controller;

import edu.java.bot.dto.LinkUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
    private final Logger logger = LoggerFactory.getLogger(BotController.class);

    @PostMapping("/updates")
    public ResponseEntity<Void> sendUpdates(
        @RequestBody LinkUpdate linkUpdate
    ) {
        logger.info("обновления получены");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
