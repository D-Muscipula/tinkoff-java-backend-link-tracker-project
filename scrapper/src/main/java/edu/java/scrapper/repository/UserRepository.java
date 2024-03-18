package edu.java.scrapper.repository;

import edu.java.scrapper.dto.TgUser;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void add(TgUser tgUser);

    void updateTgUser(TgUser tgUser);

    void remove(Long id);

    Optional<TgUser> findById(Long id);

    List<TgUser> findAll();
}
