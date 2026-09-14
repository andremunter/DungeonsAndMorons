package se.dungeonsandmorons.town;

import se.dungeonsandmorons.model.npc.Npc;

import java.util.ArrayList;
import java.util.List;

public class Town {
    private final String name;
    private final String description;
    private final List<Npc> npcs;

    public Town(String name, String description, List<Npc> npcs) {
        this.name = name;
        this.description = description;
        this.npcs = npcs;
    }

    public Npc findNpc(String query) {
        for (Npc npc : npcs) {
            if (npc.matches(query)) {
                return npc;
            }
        }
        return null;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Npc> getNpcs() { return npcs; }

}
