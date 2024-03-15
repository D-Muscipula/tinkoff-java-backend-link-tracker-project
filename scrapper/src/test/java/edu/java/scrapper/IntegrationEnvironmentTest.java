package edu.java.scrapper;

import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.dto.UserLink;
import edu.java.dto.UserState;
import edu.java.repository.LinkRepository;
import edu.java.repository.LinkRepositoryImpl;
import edu.java.repository.UserLinkRepository;
import edu.java.repository.UserLinkRepositoryImpl;
import edu.java.repository.UserRepository;
import edu.java.repository.UserRepositoryImpl;
import liquibase.database.jvm.JdbcConnection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import javax.sql.DataSource;
import java.net.URI;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void userRepositoryImplTest(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUser(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());

        JdbcClient jdbcClient = JdbcClient.create(dataSource);
        User userForAdding = new User(125L, "registered");

        UserRepository userRepository = new UserRepositoryImpl(jdbcClient);
        userRepository.add(userForAdding);
        Assertions.assertTrue(userRepository.findById(125L).isPresent());

        User foundUser = userRepository.findById(125L).get();
        Assertions.assertEquals(userForAdding, foundUser);
        userRepository.remove(125L);

        Optional<User> deletedUser = userRepository.findById(125L);
        Assertions.assertFalse(deletedUser.isPresent());
    }

    @Test
    @SneakyThrows
    public void linkRepositoryImplTest(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUser(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());

        JdbcClient jdbcClient = JdbcClient.create(dataSource);
        URI uriForAdd = URI.create("https://stackoverflow.com/questions/42/best-way-to-allow-plugins-for-a-php-application/77#77");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2024-01-31T22:22:10Z");
        OffsetDateTime lastCheckedAt = OffsetDateTime.parse("2024-02-18T16:14:29Z");
        LinkRepository linkRepository = new LinkRepositoryImpl(jdbcClient);
        linkRepository.add(new Link(-1L, uriForAdd, updatedAt, lastCheckedAt));
        Assertions.assertTrue(linkRepository.findByURL(uriForAdd).isPresent());

        Link foundLink = linkRepository.findByURL(uriForAdd).get();
        Assertions.assertEquals(uriForAdd, foundLink.url());
        Assertions.assertEquals(updatedAt, foundLink.updatedAt());
        Assertions.assertEquals(lastCheckedAt, foundLink.lastCheckedAt());

        linkRepository.removeByURL(uriForAdd);
        Optional<Link> deletedLink= linkRepository.findByURL(uriForAdd);
        Assertions.assertFalse(deletedLink.isPresent());

        linkRepository.add(new Link(-1L, uriForAdd, updatedAt, lastCheckedAt));
        Assertions.assertTrue(linkRepository.findById(2L).isPresent());
        linkRepository.removeById(2L);
        Optional<Link> deletedLink1= linkRepository.findById(2L);
        Assertions.assertFalse(deletedLink1.isPresent());
    }


    @Test
    @SneakyThrows
    public void userLinkRepositoryImplTest(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUser(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());

        JdbcClient jdbcClient = JdbcClient.create(dataSource);
        URI uriForAdd = URI.create("https://stackoverflow.com/questions/42/best-way-to-allow-plugins-for-a-php-application/77#77");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2024-01-31T22:22:10Z");
        OffsetDateTime lastCheckedAt = OffsetDateTime.parse("2024-02-18T16:14:29Z");

        User userForAdding = new User(125L, "registered");
        Link linkForAdding = new Link(10L, uriForAdd, updatedAt, lastCheckedAt);
        UserLink userLink = new UserLink(-1L, userForAdding, linkForAdding);

        UserLinkRepository userLinkRepository = new UserLinkRepositoryImpl(jdbcClient);
        userLinkRepository.add(userLink);
        Assertions.assertTrue(userLinkRepository.findById(1L).isPresent());

        UserLink foundUserLink = userLinkRepository.findById(1L).get();
        Assertions.assertEquals(foundUserLink.user(), userForAdding);
        Assertions.assertEquals(foundUserLink.link(), linkForAdding);

        UserLink foundUserLinkByUserLink= userLinkRepository.findByUserIdAndLinkId(125L, 10L)
            .get();
        Assertions.assertEquals(userLink, foundUserLinkByUserLink);

        userLinkRepository.removeByURL(uriForAdd);
        Optional<UserLink> deletedUserLink= userLinkRepository.findByURL(uriForAdd);
        Assertions.assertFalse(deletedUserLink.isPresent());

        userLinkRepository.add(new UserLink(-1L, uriForAdd, updatedAt, lastCheckedAt));
        Assertions.assertTrue(userLinkRepository.findById(2L).isPresent());
        userLinkRepository.removeById(2L);
        Optional<UserLink> deletedUserLink1= userLinkRepository.findById(2L);
        Assertions.assertFalse(deletedUserLink1.isPresent());
    }
}
