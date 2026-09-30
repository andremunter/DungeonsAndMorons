package se.dungeonsandmorons.town;

import se.dungeonsandmorons.model.npc.Npc;

import java.util.List;

public class Town {
    private final String name;
    private final String description;
    private final List<Npc> npcs;

    public String getDescription() { return description; }
    public String getName() { return name; }
    public List<Npc> getNpcs() { return npcs; }

    public Town(String name, String description, List<Npc> npcs) {
        this.name = name;
        this.description = description;
        this.npcs = npcs;
    }

    public Npc findNpc(String query) {
        return npcs.stream()
                .filter(npc -> npc.matches(query))
                .findFirst()
                .orElse(null);

    }

}
