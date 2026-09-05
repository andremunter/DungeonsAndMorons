package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.model.npc.Npc;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private String name;
    private String description;
    private int areaLevel;

    List<Npc> monsters;

    public Dungeon(String name, String description, int areaLevel, List<Npc> monsters) {
        this.name = name;
        this.description = description;
        this.areaLevel = areaLevel;
        this.monsters = monsters;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getAreaLevel() { return areaLevel; }
    public List<Npc> getMonsters() { return monsters; }

    @Override
    public String toString() {
        return String.format("""
                %s
                %s
                Area Level: %d""", name, description, areaLevel);
    }
}
