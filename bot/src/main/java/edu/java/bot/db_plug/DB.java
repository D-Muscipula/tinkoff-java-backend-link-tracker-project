package edu.java.bot.db_plug;

import edu.java.bot.db_plug.DataBase;
import java.util.ArrayList;
import java.util.List;

public class DB implements DataBase {
    @Override
    public List<String> getLinks() {
        return new ArrayList<>();
    }

}
