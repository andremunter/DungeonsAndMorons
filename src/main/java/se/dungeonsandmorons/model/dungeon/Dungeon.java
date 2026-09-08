package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.exception.ListNullOrEmptyException;
import se.dungeonsandmorons.main.GameData;
import se.dungeonsandmorons.model.npc.MonsterRarity;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.model.npc.NpcFactory;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private final String name;
    private final String description;
    private final int areaLevel;

    private final List<Npc> monsters = new ArrayList<>();

    public Dungeon(String name, String description, int areaLevel) {
        this.name = name;
        this.description = description;
        this.areaLevel = areaLevel;

    }

    public void populateDungeon(List<NpcFactory> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            throw new ListNullOrEmptyException("Trying to populate dungeon with empty or null List");
        }

        for (NpcFactory recipe : recipes) {
            Npc spawned = recipe.create(this.areaLevel, MonsterRarity.NORMAL);
            this.monsters.add(spawned);
        }

    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getAreaLevel() { return areaLevel; }
    public List<Npc> getMonsters() { return monsters; }

    @Override
    public String toString() {
        return String.format("%s, '%s', Area Level: %d%s" , name, description, areaLevel, hasPlayerClearedDungeon() ? " (Cleared)" : "");
    }

    boolean hasPlayerClearedDungeon() {
        List<Dungeon> cDungeons = GameData.player.getClearedDungeons();
        if (!cDungeons.isEmpty()) {
            for (Dungeon dung : cDungeons) {
                if (this.name.equalsIgnoreCase(dung.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
