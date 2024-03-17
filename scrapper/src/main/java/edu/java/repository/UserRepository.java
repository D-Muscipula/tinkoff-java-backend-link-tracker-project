package edu.java.repository;

import edu.java.dto.TgUser;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void add(TgUser tgUser);

    void remove(Long id);

    Optional<TgUser> findById(Long id);

    List<TgUser> findALl();
}
