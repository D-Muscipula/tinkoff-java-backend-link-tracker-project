package edu.java.bot.repository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    Map<Long, User> map = new Hashtable<>();

    public UserRepositoryImpl() {
        map.put(1L, new User(
            1L,
            null,
            new ArrayList<>() {
                {
                    add(String.valueOf(map));
                }
            }
        ));
    }

    @PostConstruct
    public void init() throws Exception {
        System.out.println("Я инит-метод " + this.getClass().getSimpleName() + " " + this);
    }

    @Override
    public User get(Long id) {
        return map.get(id);
    }

    @Override
    public void add(User user) {
        map.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        //User userFromBase = map.get(user.getId());
        map.put(user.getId(), user);
    }

}
