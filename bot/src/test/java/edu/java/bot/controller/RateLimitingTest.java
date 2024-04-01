package edu.java.bot.controller;

import dto.request.LinkUpdate;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:RateLimitingTest.properties"},
                    properties = {"spring.cache.type="})
public class RateLimitingTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("10 край, брат")
    void rateLimitingTest() throws Exception {
          String json = """
            {
              "id": 1,
              "url": "http://example.com",
              "description": "Description",
              "tgChatIds": [123]
            }

            """
        ;
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/updates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk());
        }

        mockMvc.perform(post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isTooManyRequests());
    }
}
