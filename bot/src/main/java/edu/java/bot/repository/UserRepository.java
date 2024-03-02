package edu.java.bot.repository;

public interface UserRepository {

    User get(Long id);

    void add(User user);

    void update(User user);
}
