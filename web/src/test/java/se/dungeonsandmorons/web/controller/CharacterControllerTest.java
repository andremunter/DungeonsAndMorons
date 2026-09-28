package se.dungeonsandmorons.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.repository.CharacterSaveData;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.service.SaveService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import se.dungeonsandmorons.web.dto.AllocateStatsRequest;
import se.dungeonsandmorons.web.dto.CreateCharacterRequest;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(CharacterController.class)
public class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    @DisplayName("GET /api/characters/{name} returns 200 OK when character exists")
    void getCharacterByName_WhenFound_Returns200() throws Exception {
        Kharacter gucki = new Kharacter("Gucki", CharacterClass.WARRIOR);
        CharacterSaveData saveData = new CharacterSaveData(gucki, "Densewood");

        when(saveService.loadGame("Gucki")).thenReturn(Optional.of(saveData));

        mockMvc.perform(get("/api/characters/Gucki"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.player.name").value("Gucki"))
                .andExpect(jsonPath("$.player.characterClass").value("WARRIOR"))
                .andExpect(jsonPath("$.lastTownVisited").value("Densewood"));
    }

    @Test
    @DisplayName("GET /api/characters/{name} returns 404 Not Found when character does not exist")
    void getCharacterByName_WhenNotFound_Returns404() throws Exception {
        when(saveService.loadGame(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/characters/NonExistentCharacter"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/characters returns 201 Created when payload is valid")
    void createCharacter_ValidPayload_Returns201() throws Exception {
        CreateCharacterRequest request = new CreateCharacterRequest("Ragnar", "WARRIOR");

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ragnar"))
                .andExpect(jsonPath("$.characterClass").value("WARRIOR"));
    }

    @Test
    @DisplayName("POST /api/characters returns 400 Bad Request when name contains invalid characters")
    void createCharacter_InvalidName_Returns400() throws Exception {
        // Violates @Pattern(regexp = "^[a-zA-Z0-9_-]+$") due to spaces/symbols
        CreateCharacterRequest request = new CreateCharacterRequest("Invalid Name!#", "WARRIOR");

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("DELETE /api/characters/{name} returns 204 No Content when deleted")
    void deleteCharacter_WhenExists_Returns204() throws Exception {
        when(saveService.deleteSave("Gucki")).thenReturn(true);

        mockMvc.perform(delete("/api/characters/Gucki"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/characters/{name} returns 404 Not Found when character does not exist")
    void deleteCharacter_WhenNotFound_Returns404() throws Exception {
        when(saveService.deleteSave(anyString())).thenReturn(false);

        mockMvc.perform(delete("/api/characters/UnknownHero"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/characters/{name}/stats returns 200 OK on valid allocation")
    void allocateStats_ValidPoints_Returns200() throws Exception {
        Kharacter warrior = new Kharacter("Gucki", CharacterClass.WARRIOR);
        // Ensure character has unallocated stat points
        warrior.addExperience(1000);
        when(saveService.loadGame("Gucki")).thenReturn(Optional.of(new CharacterSaveData(warrior, "Densewood")));

        AllocateStatsRequest request = new AllocateStatsRequest(1, 0, 0, 0);

        mockMvc.perform(patch("/api/characters/Gucki/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/characters/{name}/stats returns 400 Bad Request on negative stat values")
    void allocateStats_NegativePoints_Returns400() throws Exception {
        AllocateStatsRequest request = new AllocateStatsRequest(-2, 0, 0, 0);

        mockMvc.perform(patch("/api/characters/Gucki/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

}
