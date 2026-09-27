package se.dungeonsandmorons.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.repository.CharacterSaveData;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.service.SaveService;
import se.dungeonsandmorons.web.dto.AllocateStatsRequest;
import se.dungeonsandmorons.web.dto.CreateCharacterRequest;

import java.net.URI;
import java.util.List;

@Tag(name = "Characters", description = "Character save and retrieval operations")
@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final SaveService saveService;

    public CharacterController(SaveService saveService) {
        this.saveService = saveService;
    }

    @Operation(summary = "List all saved characters")
    @GetMapping
    public List<CharacterSummary> getAllSaves() {
        return saveService.getAvailableSaves();
    }

    @Operation(summary = "Get full character profile by name")
    @GetMapping("/{name}")
    public ResponseEntity<CharacterSaveData> getCharacter(@PathVariable String name) {
        return saveService.loadGame(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create and save a new character")
    @PostMapping
    public ResponseEntity<?> createCharacter(@Valid @RequestBody CreateCharacterRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return ResponseEntity.badRequest().body("Character name cannot be blank");
        }

        String trimmedName = request.name().trim();
        if (saveService.saveExists(trimmedName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A character with the name '" + trimmedName + "' already exists.");
        }

        CharacterClass chosenClass;
        try {
            chosenClass = CharacterClass.valueOf(request.characterClass().trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
             return ResponseEntity.badRequest().body("Invalid character class: " + request.characterClass());
        }

        Kharacter newCharacter = new Kharacter(trimmedName, chosenClass);
        String startingTown = "Densewood";

        saveService.saveGame(newCharacter, startingTown);

        URI location = URI.create("/api/characters/" + newCharacter.getName());
        return ResponseEntity.created(location).body(new CharacterSummary(
                newCharacter.getName(),
                newCharacter.getCharacterClass().name(),
                newCharacter.getLevel(),
                startingTown
        ));
    }

    @Operation(summary = "Delete a character save")
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String name) {
        boolean deleted = saveService.deleteSave(name);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Allocate unallocated stat points to a character")
    @PatchMapping("/{name}/stats")
    public ResponseEntity<CharacterSaveData> allocateStats(
            @PathVariable String name,
            @Valid @RequestBody AllocateStatsRequest request
    ) {
        var saveOpt = saveService.loadGame(name);
        if (saveOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CharacterSaveData saveData = saveOpt.get();
        Kharacter player = saveData.player();

        if (request.totalPoints() == 0) {
            throw new IllegalArgumentException("You must allocate at least 1 stat point.");
        }

        if (request.totalPoints() > player.getUnallocatedStatPoints()) {
            throw new IllegalArgumentException(String.format(
                    "Cannot allocate %d points. Character '%s' only has %d unallocated points.",
                    request.totalPoints(), name, player.getUnallocatedStatPoints()
            ));
        }

        player.allocateStats(request.strength(), request.agility(), request.intelligence(), request.vitality());
        saveService.saveGame(player, saveData.lastTownVisited());

        return ResponseEntity.ok(new CharacterSaveData(player, saveData.lastTownVisited()));
    }

}
