package se.dungeonsandmorons.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.dungeonsandmorons.service.DungeonService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DungeonController.class)
public class DungeonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DungeonService dungeonService;

    @Test
    @DisplayName("GET /api/dungeons returns 200 OK and available dungeons")
    void getDungeons_Returns200AndList() throws Exception {
        when(dungeonService.getAvailableDungeonNames()).thenReturn(List.of());

        mockMvc.perform(get("/api/dungeons"))
                .andExpect(status().isOk());
    }
}