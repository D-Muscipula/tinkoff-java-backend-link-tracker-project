package edu.java.bot;

import java.util.List;

public interface DataBase {
    List<String> getLinks(int id);
    void deleteLink(int id, String link);
    void put (int id, String link);
}
