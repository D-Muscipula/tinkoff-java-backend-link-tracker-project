package edu.java.bot.dto.response;

import java.util.List;

/**
 * ListLinksResponse
 */

public record ListLinksResponse(List<LinkResponse> links, Integer size) {
}

