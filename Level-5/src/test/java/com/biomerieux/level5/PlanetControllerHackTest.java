package com.biomerieux.level5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test FAILS against the vulnerable PlanetController.
 * Your goal is to fix it so it turns green, while PlanetControllerTest keeps passing too.
 * <p>
 * This automated check is a server-side proxy for the real browser-based exploit
 * described below. For the full effect, also try it live:
 * <ol>
 *   <li>Run: {@code mvn -pl Level-5 spring-boot:run}</li>
 *   <li>Open http://localhost:8080/ and enter the following in the planet field:
 *   {@code &ltimg src="x" onerror="document.body.innerHTML = 'Website is hacked';"&gt}</li>
 * </ol>
 * The blocklist regex only strips {@code <>{}[]}, so the HTML entities
 * {@code &lt} / {@code &gt} (no trailing semicolon) sail through untouched. The server
 * renders them unescaped (th:utext), and the page's own JavaScript later reads that text
 * back and re-injects it via {@code innerHTML}, which re-parses it as real HTML and fires
 * the {@code onerror} handler.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PlanetControllerHackTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1_entityBasedXssBypassesTheBlocklist() throws Exception {
        String payload = "&ltimg src=\"x\" onerror=\"document.body.innerHTML = 'Website is hacked';\"&gt";

        MvcResult result = mockMvc.perform(post("/").param("planet", payload))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        // The payload must not reach the page unescaped/unblocked.
        assertFalse(body.contains(payload),
                "The XSS payload must be blocked or HTML-escaped before being rendered");
    }
}
