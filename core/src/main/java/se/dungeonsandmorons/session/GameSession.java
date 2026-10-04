package se.dungeonsandmorons.session;

import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.dungeon.DungeonRun;

import java.time.Instant;

public class GameSession {
    private final String sessionId;
    private final Kharacter player;
    private DungeonRun activeRun;
    private CombatEngine activeCombat;
    private Instant lastActive;

    public GameSession(String sessionId, Kharacter player) {
        this.sessionId = sessionId;
        this.player = player;
        this.lastActive = Instant.now();
    }

    public boolean isInCombat() { return activeCombat != null; }
    public boolean isInDungeon() { return activeRun != null; }

    public DungeonRun getActiveRun() { return activeRun; }
    public void startDungeonRun(Dungeon dungeon) { this.activeRun = new DungeonRun(dungeon); }
    public void endDungeonRun() { this.activeRun = null; }

    public CombatEngine getActiveCombat() { return activeCombat; }
    public void startCombat(CombatEngine engine) { this.activeCombat = engine; }
    public void endCombat() { this.activeCombat = null; }

    public String getSessionId() { return sessionId; }
    public Kharacter getPlayer() { return player; }

}
