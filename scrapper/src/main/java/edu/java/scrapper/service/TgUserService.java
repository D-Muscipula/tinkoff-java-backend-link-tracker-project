package edu.java.scrapper.service;

import edu.java.scrapper.dto.TgUser;

public interface TgUserService {
    void register(long tgUserId);

    void unregister(long tgUserId);

    void update(TgUser tgUser);
}
