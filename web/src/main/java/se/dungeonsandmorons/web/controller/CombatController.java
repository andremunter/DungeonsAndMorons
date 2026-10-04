package se.dungeonsandmorons.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.combat.CombatState;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.dungeon.DungeonRun;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.service.CombatService;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.session.GameSession;
import se.dungeonsandmorons.session.PlayerSessionService;
import se.dungeonsandmorons.web.dto.CombatActionRequest;
import se.dungeonsandmorons.web.dto.CombatStateResponse;
import se.dungeonsandmorons.web.dto.CombatantStatusDto;

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
        return ResponseEntity.ok(toResponseDto(engine));
    }

    private CombatStateResponse toResponseDto(CombatEngine engine) {
        Kharacter player = engine.getPlayer();
        Monster enemy = engine.getEnemy();

        CombatantStatusDto playerDto = new CombatantStatusDto(
                player.getName(),
                player.getLevel(),
                player.getCurrentHealth(),
                player.getMaxHealth(),
                player.getCurrentMana(),
                player.getMaxMana(),
                player.getArmor()
        );

        CombatantStatusDto enemyDto = new CombatantStatusDto(
                enemy.getName(),
                enemy.getLevel(),
                enemy.getCurrentHealth(),
                enemy.getMaxHealth(),
                0, // Monsters have no mana
                0,
                enemy.getArmor()
        );

        return new CombatStateResponse(
                engine.getState().name(),
                engine.getRoundCount(),
                playerDto,
                enemyDto,
                player.getSkillNames(),
                engine.getCombatLog().stream().map(Object::toString).toList(),
                engine.getState().isTerminal()
        );
    }

}
