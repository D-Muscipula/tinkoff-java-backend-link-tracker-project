package edu.java.bot.client_test;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public abstract class AbstractClientTest {
    protected static WireMockServer wireMockServer;
    @BeforeAll
    public static void initWireMockServer() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        configureFor("localhost", 8080);
    }

    @AfterAll
    public static void close() {
        wireMockServer.stop();
    }

    abstract void init();
}
