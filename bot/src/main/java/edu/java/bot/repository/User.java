package edu.java.bot.repository;

import java.util.List;

public record User(Long id, UserState userState, List<String> links) {
}
