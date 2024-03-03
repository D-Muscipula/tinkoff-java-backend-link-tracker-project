package edu.java.contoller;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapperController {
    private final Logger logger = LoggerFactory.getLogger(ScrapperController.class);

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
        @PathVariable("id") Integer tgChatId
    ) {
        logger.info("чат зарегистрирован");
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable("id") Integer tgChatId
    ) {
        logger.info("чат успешно удалён");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
        @RequestHeader("Tg-Chat-Id") Integer tgChatId
    ) {
        logger.info("ссылки успешно получены");
        List<LinkResponse> someLinks = new ArrayList<>();
        return new ResponseEntity<>(
            new ListLinksResponse(someLinks, 0),
            HttpStatus.OK
        );
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        logger.info("ссылка успешна добавлена");
        return new ResponseEntity<>(
            new LinkResponse(tgChatId, addLinkRequest.link()),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        logger.info("ссылка успешна удалена");
        return new ResponseEntity<>(
            new LinkResponse(tgChatId, removeLinkRequest.link()),
            HttpStatus.OK
        );
    }
}
