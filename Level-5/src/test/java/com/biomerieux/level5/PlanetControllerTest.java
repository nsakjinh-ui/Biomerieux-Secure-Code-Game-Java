package com.biomerieux.level5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndexRoute() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testGetPlanetInfoInvalidPlanet() {
        assertEquals("Unknown planet.", PlanetController.getPlanetInfo("Pluto"));
    }

    @Test
    void testGetPlanetInfoValidPlanet() {
        assertEquals("The smallest and fastest planet in the Solar System.",
                PlanetController.getPlanetInfo("Mercury"));
    }

    @Test
    void testIndexValidPlanet() throws Exception {
        mockMvc.perform(post("/").param("planet", "Venus"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.startsWith("<!DOCTYPE html>")));
    }

    @Test
    void testIndexMissingPlanet() throws Exception {
        mockMvc.perform(post("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h2>Please enter a planet name.</h2>"));
    }

    @Test
    void testIndexEmptyPlanet() throws Exception {
        mockMvc.perform(post("/").param("planet", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("<h2>Please enter a planet name.</h2>"));
    }

    @Test
    void testIndexActiveContentPlanet() throws Exception {
        mockMvc.perform(post("/").param("planet", "<script ...>"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h2>Blocked</h2></p>"));
    }
}
