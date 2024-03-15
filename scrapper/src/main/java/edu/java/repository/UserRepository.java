package edu.java.repository;

import edu.java.dto.User;
import java.util.Optional;

public interface UserRepository {

    void add(User user);

    void remove(Long id);

    Optional<User> findById(Long id);
}
