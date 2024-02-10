package edu.java.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DB implements DataBase{
    Map<Integer, List<String>> links = new Hashtable<>();
    @Override
    public List<String> getLinks(int id) {
        return new ArrayList<>(links.get(id));
    }

    public void put(int id, String link) {
        links.get(id).add(link);
    }

    @Override
    public void deleteLink(int id, String link) {
        links.get(id).remove(link);
    }
}
