package se.dungeonsandmorons.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dungeonsandmorons.repository.CharacterSaveData;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.service.SaveService;

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

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String name) {
        boolean deleted = saveService.deleteSave(name);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
