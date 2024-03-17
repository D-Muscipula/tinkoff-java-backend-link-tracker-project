package edu.java.scrapper.domain.service;

public interface TgUserService {
    void register(long tgUserId);

    void unregister(long tgUserId);
}
