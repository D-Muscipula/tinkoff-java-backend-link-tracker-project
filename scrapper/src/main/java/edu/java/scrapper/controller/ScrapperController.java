package edu.java.scrapper.controller;

import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.request.TgUserUpdate;
import dto.response.ApiErrorResponse;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import dto.response.TgUserResponse;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.exceptions.ChatDoesntExistException;
import edu.java.scrapper.exceptions.ThereIsNoSuchLinkException;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.jdbc.JdbcTgUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapperController {
    private final JdbcLinkService jdbcLinkService;
    private final JdbcTgUserService jdbcTgUserService;
    private final Logger logger = LoggerFactory.getLogger(ScrapperController.class);

    @Autowired
    public ScrapperController(JdbcLinkService jdbcLinkService, JdbcTgUserService jdbcTgUserService) {
        this.jdbcLinkService = jdbcLinkService;
        this.jdbcTgUserService = jdbcTgUserService;
    }

    @PostMapping("/tg-chat/{id}")
    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409",
                     description = "Чат уже зарегистрирован",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> registerChat(
        @Parameter(description = "Id чата в telegram", required = true)
        @PathVariable("id") Long tgChatId
    ) {
        try {
            jdbcTgUserService.register(tgChatId);
            logger.info("чат " + tgChatId + "зарегистрирован");
        } catch (ChatAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удалён"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404",
                     description = "Чат не существует",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteChat(
        @Parameter(description = "Id чата в telegram", required = true)
        @PathVariable("id") Long tgChatId
    ) {
        try {
            jdbcTgUserService.unregister(tgChatId);
            logger.info("чат успешно удалён");
        } catch (ChatDoesntExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылки успешно получены",
                     content = @Content(schema = @Schema(implementation = ListLinksResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Пользователь не зарегистрирован",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Повторное добавление ссылки",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ListLinksResponse> getLinks(
        @RequestHeader("Tg-Chat-Id") Long tgChatId
    ) {
        List<LinkResponse> someLinks = new ArrayList<>();
        try {
            List<Link> links = jdbcLinkService.listAll(tgChatId);
            links.forEach((link) -> someLinks.add(new LinkResponse(link.id(), link.url())));
            logger.info("ссылки успешно получены");
        } catch (ChatDoesntExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
            new ListLinksResponse(someLinks, someLinks.size()),
            HttpStatus.OK
        );
    }

    @PostMapping("/links")
    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class))),
        @ApiResponse(responseCode = "202", description = "Ожидание ссылки",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Пользователь не зарегистрирован",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Ссылка на добавление не ожидалась",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<LinkResponse> addLink(
        @Parameter(description = "Id чата в telegram", required = true)
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        try {
            jdbcLinkService.add(tgChatId, addLinkRequest.link());
            logger.info("ссылка успешна добавлена");
        } catch (ChatDoesntExistException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
            new LinkResponse(tgChatId, addLinkRequest.link()),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/links")
    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class))),
        @ApiResponse(responseCode = "202", description = "Ожидание ссылки",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Пользователь не зарегистрирован",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Ссылка не найдена",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Ссылка на удаление не ожидалась",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<LinkResponse> deleteLink(
        @Parameter(description = "Id чата в telegram", required = true)
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        try {
            jdbcLinkService.remove(tgChatId, removeLinkRequest.link());
            logger.info("ссылка успешна удалена");
        } catch (ChatDoesntExistException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (ThereIsNoSuchLinkException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
            new LinkResponse(tgChatId, removeLinkRequest.link()),
            HttpStatus.OK
        );
    }

    @PostMapping("/state")
    public ResponseEntity<Void> updateChat(
        @RequestBody TgUserUpdate tgUserUpdate
    ) {
        try {
            TgUser tgUser = new TgUser(tgUserUpdate.userChatId(), tgUserUpdate.userState());
            jdbcTgUserService.update(tgUser);
            logger.info("состояние " + tgUser.userChatId() + "изменено");
        } catch (ChatDoesntExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/state")
    public ResponseEntity<TgUserResponse> getState(
        @RequestHeader("Tg-Chat-Id") Long tgChatId
    ) {
        Optional<TgUser> tgUser = jdbcTgUserService.findById(tgChatId);
        return tgUser.map(user -> new ResponseEntity<>(
                new TgUserResponse(user.userChatId(), user.userState()),
                HttpStatus.OK
        )).orElseGet(() -> new ResponseEntity<>(
                new TgUserResponse(tgChatId, "unregistered"),
                HttpStatus.OK
        ));
    }
}
