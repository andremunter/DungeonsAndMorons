package se.dungeonsandmorons.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.dungeon.DungeonRun;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.service.CombatService;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.session.GameSession;
import se.dungeonsandmorons.session.PlayerSessionService;
import se.dungeonsandmorons.web.dto.CombatStateResponse;
import se.dungeonsandmorons.web.dto.DungeonResponse;
import se.dungeonsandmorons.web.dto.ExploreResponse;
import se.dungeonsandmorons.web.dto.MonsterSummaryResponse;

import java.util.List;

@Tag(name = "Dungeons", description = "Query available dungeons and encounters information")
@RestController
@RequestMapping("/api/dungeons")
public class DungeonController {

    private final DungeonService dungeonService;
    private final PlayerSessionService sessionService;
    private final CombatService combatService;

    public DungeonController(DungeonService dungeonService,
                             PlayerSessionService sessionService,
                             CombatService combatService) {
        this.dungeonService = dungeonService;
        this.sessionService = sessionService;
        this.combatService = combatService;
    }

    @PostMapping("/explore")
    public ResponseEntity<ExploreResponse> explore(@Parameter(hidden = true) @CookieValue(name = "GAME_SESSION") String sessionId) {
        GameSession session = sessionService.getSession(sessionId);

        if (!session.isInDungeon()) {
            throw new IllegalStateException("Cannot explore: Not currently in a dungeon.");
        }
        if (session.isInCombat()) {
            throw new IllegalStateException("Cannot explore: Currently in combat.");
        }

        DungeonRun run = session.getActiveRun();
        List<Monster> remainingMonsters = run.getDungeon().getMonsters();

        if (remainingMonsters.isEmpty()) {
            return ResponseEntity.ok(new ExploreResponse("CLEARED", "The dungeon is clear! You may leave safely.", null));
        }

        Monster nextMonster = remainingMonsters.getFirst();
        combatService.startCombat(session, nextMonster);

        CombatEngine engine = session.getActiveCombat();
        CombatStateResponse combatState = CombatStateResponse.from(engine);

        return ResponseEntity.ok(new ExploreResponse(
                "COMBAT",
                "You encountered a " + nextMonster.getName() + "!",
                combatState
        ));
    }

    @PostMapping("/{name}/enter")
    public ResponseEntity<String> enterDungeon(
            @Parameter(hidden = true) @CookieValue(name = "GAME_SESSION") String sessionId,
            @PathVariable String name) {

        GameSession session =  sessionService.getSession(sessionId);

        if (session.isInDungeon()) {
            throw new IllegalStateException("Already in dungeon. Leave or finish your active run first.");
        }

        String formattedName = name.replace("-", " ").trim();
        Dungeon dungeon = dungeonService.findDungeonByName(formattedName);
        session.startDungeonRun(dungeon);

        return ResponseEntity.ok("Entered " + dungeon.getName() + ".");
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leaveDungeon(@Parameter(hidden = true) @CookieValue(name = "GAME_SESSION") String sessionId) {

        GameSession session = sessionService.getSession(sessionId);

        if (!session.isInDungeon()) {
            throw new IllegalStateException("Cannot leave: Not currently in a dungeon.");
        }
        if (session.isInCombat()) {
            throw new IllegalStateException("Cannot leave: Must finish or flee current battle.");
        }

        int earnedExp = session.getActiveRun().getPendingExp();
        dungeonService.leaveDungeon(session, "densewood");

        return ResponseEntity.ok("Safely left the dungeon. Earned " + earnedExp + " EXP!");
    }

    @Operation(summary = "List all discoverable dungeons with encounter summaries")
    @GetMapping
    public List<DungeonResponse> getAllDungeons() {
        return dungeonService.getAvailableDungeonNames().stream()
                .map(dungeonService::findDungeonByName)
                .map(this::toResponse)
                .toList();
    }

    @Operation(summary = "Get dungeon details and monster spawns by name")
    @GetMapping("/{name}")
    public DungeonResponse getDungeonByName(@PathVariable String name) {
        String formattedName = name.replace("-", " ").trim();
        Dungeon dungeon = dungeonService.findDungeonByName(formattedName);
        return toResponse(dungeon);
    }

    private DungeonResponse toResponse(Dungeon dungeon) {
        List<MonsterSummaryResponse> monsters = dungeon.getMonsters().stream()
                .map(m -> new MonsterSummaryResponse(
                        m.getName(),
                        m.getMaxHealth(),
                        m.getExpOnDeath(),
                        m.getRarity().name()
                ))
                .toList();

        return new DungeonResponse(
                dungeon.getName(),
                dungeon.getDescription(),
                dungeon.getAreaLevel(),
                monsters.size(),
                monsters
        );
    }

}
