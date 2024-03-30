package edu.java.bot.service;

import edu.java.bot.repository.User;

public interface UserService {
    User get(Long id);

    void add(User user);

    void update(User user);
}
