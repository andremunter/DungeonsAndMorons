package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.model.monster.Monster;

public class DungeonRun {
    private final Dungeon dungeon;
    private int pendingExp = 0;
    // private final list<Item> pendingLoot = new ArrayList<>(); // Reserved for loot system implementation
    private int roomsExplored = 0;

    public DungeonRun(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public int applyDeathPenalty(double penaltyRate) {

        /// Applies a loss penalty to pending EXP (e.g. penaltyRate = 0.5 for 50% loss, 1.0 for 100% loss).
        /// Returns the finalized EXP awarded to the player.

        double retainedRatio = Math.max(0.0, 1.0 - penaltyRate);
        this.pendingExp = (int) Math.round(this.pendingExp * retainedRatio);
        return this.pendingExp;
    }

    public void recordVictory(Monster deafeatedMonster) {
        pendingExp += deafeatedMonster.getExpOnDeath();
        this.dungeon.removeMonster(deafeatedMonster);
    }

    public boolean isDungeonCleared() {
        return dungeon.getMonsters().isEmpty();
    }

    public Dungeon getDungeon() { return dungeon; }
    public int getPendingExp() { return pendingExp; }

}
