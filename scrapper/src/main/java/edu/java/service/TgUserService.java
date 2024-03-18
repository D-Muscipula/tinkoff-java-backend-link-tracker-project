package edu.java.service;

import edu.java.dto.TgUser;

public interface TgUserService {
    void register(long tgUserId);

    void unregister(long tgUserId);

    void update(TgUser tgUser);
}
