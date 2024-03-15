package edu.java.repository;

import edu.java.dto.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void add(User user);

    void remove(User user);

    Optional<User> findById(Long id);
}
