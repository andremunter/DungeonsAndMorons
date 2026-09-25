package se.dungeonsandmorons.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.repository.CharacterSaveData;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.service.SaveService;
import se.dungeonsandmorons.web.dto.CreateCharacterRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final SaveService saveService;

    public CharacterController(SaveService saveService) {
        this.saveService = saveService;
    }

    @GetMapping
    public List<CharacterSummary> getAllSaves() {
        return saveService.getAvailableSaves();
    }

    @GetMapping("/{name}")
    public ResponseEntity<CharacterSaveData> getCharacter(@PathVariable String name) {
        return saveService.loadGame(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCharacter(@RequestBody CreateCharacterRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return ResponseEntity.badRequest().body("Character name cannot be blank");
        }

        if (saveService.saveExists(request.name().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A character with the name '" + request.name() + "' already exists.");
        }

        CharacterClass chosenClass;
        try {
            chosenClass = CharacterClass.valueOf(request.characterClass().trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
             return ResponseEntity.badRequest().body("Invalid character class: " + request.characterClass());
        }

        Kharacter newCharacter = new Kharacter(request.name().trim(), chosenClass);
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

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String name) {
        boolean deleted = saveService.deleteSave(name);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
