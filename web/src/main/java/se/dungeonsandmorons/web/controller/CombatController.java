package se.dungeonsandmorons.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.combat.CombatState;
import se.dungeonsandmorons.model.dungeon.DungeonRun;
import se.dungeonsandmorons.service.CombatService;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.session.GameSession;
import se.dungeonsandmorons.session.PlayerSessionService;
import se.dungeonsandmorons.web.dto.CombatActionRequest;
import se.dungeonsandmorons.web.dto.CombatStateResponse;

@RestController
@RequestMapping("/api/combat")
public class CombatController {
    private final PlayerSessionService sessionService;
    private final CombatService combatService;
    private final DungeonService dungeonService;

    public CombatController(PlayerSessionService sessionService,
                            CombatService combatService,
                            DungeonService dungeonService) {
        this.sessionService = sessionService;
        this.combatService = combatService;
        this.dungeonService = dungeonService;
    }

    @PostMapping("/action")
    public ResponseEntity<CombatStateResponse> executeAction(
            @CookieValue(name = "GAME_SESSION") String sessionId,
            @RequestBody CombatActionRequest request
    ) {

        GameSession session = sessionService.getSession(sessionId);
        CombatEngine engine = combatService.executeTurn(session, request.action(), request.skillName());

        if (engine.getState() == CombatState.DEFEAT) {
            dungeonService.handlePlayerDeath(session, "densewood");
        } else if (engine.getState() == CombatState.VICTORY) {
            DungeonRun run = session.getActiveRun();
            if (run != null) {
                run.recordVictory(engine.getEnemy());
            }
        }
        return ResponseEntity.ok(CombatStateResponse.from(engine));
    }
}
