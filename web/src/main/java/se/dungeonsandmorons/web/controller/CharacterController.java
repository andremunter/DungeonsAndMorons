package se.dungeonsandmorons.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.repository.CharacterSaveData;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.service.SaveService;
import se.dungeonsandmorons.session.GameSession;
import se.dungeonsandmorons.session.PlayerSessionService;
import se.dungeonsandmorons.web.dto.AllocateStatsRequest;
import se.dungeonsandmorons.web.dto.CreateCharacterRequest;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Tag(name = "Characters", description = "Character save and retrieval operations")
@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final SaveService saveService;
    private final PlayerSessionService sessionService;

    public CharacterController(SaveService saveService, PlayerSessionService sessionService) {
        this.sessionService = sessionService;
        this.saveService = saveService;
    }

    @Operation(summary = "Select and load a character into an active session")
    @PostMapping("/{name}/select")
    public ResponseEntity<CharacterSummary> selectCharacter(@PathVariable String name) {
        String sessionId = UUID.randomUUID().toString();

        // Loads character into hot memory (ConcurrentHashMap)
        GameSession session = sessionService.loadCharacter(sessionId, name);

        // HttpOnly session cookie
        ResponseCookie cookie = ResponseCookie.from("GAME_SESSION", sessionId)
                .httpOnly(true)
                .secure(false) // True in production with HTTPS
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(12))
                .build();

        Kharacter player = session.getPlayer();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new CharacterSummary(
                        player.getName(),
                        player.getCharacterClass().name(),
                        player.getLevel(),
                        "Densewood",
                        player.getCurrentHealth(),
                        player.getMaxHealth(),
                        player.getCurrentMana(),
                        player.getMaxMana(),
                        player.getArmor(),
                        player.getStrength(),
                        player.getAgility(),
                        player.getIntelligence(),
                        player.getVitality()
                ));
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
                startingTown,
                newCharacter.getCurrentHealth(),
                newCharacter.getMaxHealth(),
                newCharacter.getCurrentMana(),
                newCharacter.getMaxMana(),
                newCharacter.getArmor(),
                newCharacter.getStrength(),
                newCharacter.getAgility(),
                newCharacter.getIntelligence(),
                newCharacter.getVitality()
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

    @Operation(summary = "Clear the current acive session, effectively logging out the player")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie clearedCookie = ResponseCookie.from("GAME_SESSION")
                .httpOnly(true)
                .secure(false) // True in production with HTTPS
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearedCookie.toString())
                .build();
    }

}
