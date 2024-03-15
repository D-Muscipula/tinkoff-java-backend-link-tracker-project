package edu.java.scrapper;

import edu.java.dto.User;
import edu.java.dto.UserState;
import edu.java.repository.UserRepository;
import edu.java.repository.UserRepositoryImpl;
import liquibase.database.jvm.JdbcConnection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
            List.of("databasechangelog", "databasechangeloglock", "links", "users", "users_links"),
            tables
        );
    }

    @Test
    @SneakyThrows
    public void insertUserTest(){
        JdbcConnection connection = new JdbcConnection(
            DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUser(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());

        JdbcClient jdbcClient = JdbcClient.create(dataSource);
        User user = new User(125L, UserState.REGISTERED);
        UserRepository userRepository = new UserRepositoryImpl(jdbcClient);
        userRepository.add(user);
        User user1 = userRepository.findById(125L).get();
        System.out.println(user1);
    }
}
