package edu.java.contoller;

import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.ApiErrorResponse;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.ArrayList;
import java.util.List;
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
//    private final Logger logger = LoggerFactory.getLogger(ScrapperController.class);

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
//        logger.info("чат зарегистрирован");
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
//        logger.info("чат успешно удалён");
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
//        logger.info("ссылки успешно получены");
        List<LinkResponse> someLinks = new ArrayList<>();
        return new ResponseEntity<>(
            new ListLinksResponse(someLinks, 0),
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
//        logger.info("ссылка успешна добавлена");
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
//        logger.info("ссылка успешна удалена");
        return new ResponseEntity<>(
            new LinkResponse(tgChatId, removeLinkRequest.link()),
            HttpStatus.OK
        );
    }
}
