package edu.java.scrapper.controller;

import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:RateLimitingTest.properties"},
                    properties = {"spring.cache.type="})
public class RateLimitingTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("10 край, брат")
    void rateLimitingTest() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/state").header("Tg-Chat-Id", "123")).andExpect(status().isOk());
        }

        mockMvc.perform(get("/state").header("Tg-Chat-Id", "123")).andExpect(status().isTooManyRequests());
    }
}
