package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.exception.ListNullOrEmptyException;
import se.dungeonsandmorons.model.monster.MonsterRarity;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.model.monster.MonsterFactory;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private final String name;
    private final String description;
    private final int areaLevel;
    private boolean cleared;

    private final List<Monster> monsters = new ArrayList<>();

    public Dungeon(String name, String description, int areaLevel) {
        this.name = name;
        this.description = description;
        this.areaLevel = areaLevel;
        this.cleared = false;
    }

    public void populateDungeon(List<MonsterFactory> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            throw new ListNullOrEmptyException("Trying to populate dungeon with empty or null List");
        }

        for (MonsterFactory recipe : recipes) {
            Monster spawned = recipe.create(this.areaLevel, MonsterRarity.NORMAL);
            this.monsters.add(spawned);
        }

    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getAreaLevel() { return areaLevel; }
    public List<Monster> getMonsters() { return monsters; }
    public boolean isCleared() { return cleared; }

    public void setCleared(boolean cleared) { this.cleared = cleared; }

    @Override
    public String toString() {
        return String.format("%s, '%s', Area Level: %d%s" , name, description, areaLevel, cleared ? " (Cleared)" : "");
    }

}
