package edu.java.scrapper.domain.service;

import edu.java.scrapper.dto.TgUser;
import java.util.Optional;

public interface TgUserService {
    void register(long tgUserId);

    void unregister(long tgUserId);

    void update(TgUser tgUser);

    Optional<TgUser> findById(Long tgChatId);
}
