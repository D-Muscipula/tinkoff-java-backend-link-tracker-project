package edu.java.bot.repository;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class LinkRepositoryImpl implements LinkRepository {
    Map<Long, User> map = new Hashtable<>();

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
        User userFromBase = map.get(user.getId());
        //if (user.)
        map.put(user.getId(), user);
    }

    private void removeLinks() {

    }

    //private void
}
