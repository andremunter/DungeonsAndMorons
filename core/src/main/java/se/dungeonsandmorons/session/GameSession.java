package se.dungeonsandmorons.session;

import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.dungeon.Dungeon;

import java.time.Instant;

public class GameSession {
    private final String sessionId;
    private final Kharacter player;
    private Dungeon currentDungeon;
    private CombatEngine activeCombat;
    private Instant lastActive;

    public GameSession(String sessionId, Kharacter player) {
        this.sessionId = sessionId;
        this.player = player;
        this.lastActive = Instant.now();
    }

    public boolean isInCombat() { return activeCombat != null; }
    public boolean isInDungeon() { return currentDungeon != null; }

    public void startCombat(CombatEngine engine) { this.activeCombat = engine; }
    public void endCombat() { this.activeCombat = null; }

    public Kharacter getPlayer() { return player; }
    public CombatEngine getActiveCombat() { return activeCombat; }
}
