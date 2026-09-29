package se.dungeonsandmorons.service;

import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.dungeon.DungeonRegistry;

import java.util.List;

public class DungeonService {

    public List<String> getAvailableDungeonNames() {
        return DungeonRegistry.getAllDungeonIds();
    }

    public Dungeon findDungeonByName(String name) {
        Dungeon dungeon = DungeonRegistry.create(name);
        if (dungeon == null) {
            System.out.println("Could not find specified dungeon.");
            return null;
        }
        return dungeon;
    }

}
