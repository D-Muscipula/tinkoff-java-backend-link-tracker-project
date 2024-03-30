package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import dto.request.LinkUpdate;
import dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
    private final Logger logger = LoggerFactory.getLogger(BotController.class);
    private final TelegramBot telegramBot;

    @Autowired
    public BotController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/updates")
    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Обновление обработано"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404",
                     description = "Ссылка не найдена",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "429",
                     description = "Слишком много запросов",
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))

    })
    public ResponseEntity<Void> sendUpdates(
        @RequestBody LinkUpdate linkUpdate
    ) {
        logger.info("обновления получены");
        for (var i : linkUpdate.tgChatIds()) {
            String message = linkUpdate.description();
            telegramBot.execute(new SendMessage(i, message));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
