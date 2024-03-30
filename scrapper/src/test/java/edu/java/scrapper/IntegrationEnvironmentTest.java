package edu.java.scrapper;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import liquibase.database.jvm.JdbcConnection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegrationEnvironmentTest extends IntegrationTest{
    @SneakyThrows
    @Test
    void creatingTablesTest() {
        JdbcConnection connection = new JdbcConnection(
            DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()));
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String[] types = {"TABLE"};
        ResultSet resultSet = databaseMetaData
            .getTables(null, null, "%", types);
        List<String> tables = new ArrayList<>();
        while (resultSet.next()) {
            tables.add(resultSet.getString(3));
            System.out.println(resultSet.getMetaData().getTableName(1));
        }
        Assertions.assertEquals(
            List.of("databasechangelog", "databasechangeloglock", "link", "tg_user", "users_links"),
            tables
        );
    }
}
