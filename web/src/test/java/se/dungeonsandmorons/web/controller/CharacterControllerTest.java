package se.dungeonsandmorons.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.service.SaveService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CharacterController.class)
public class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SaveService saveService;

    @Test
    @DisplayName("GET /api/characters returns 200 ok and character list")
    void getAllCharacters_Returns200AndList() throws Exception {
        when(saveService.getAvailableSaves()).thenReturn((List.of(
                new CharacterSummary("Gucki", "WARRIOR", 5, "Densewood")
        )));

        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gucki"))
                .andExpect(jsonPath("$[0].level").value(5))
                .andExpect(jsonPath("$[0].characterClass").value("WARRIOR"));

    }

}
