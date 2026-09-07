package se.dungeonsandmorons.service;

import se.dungeonsandmorons.main.GameData;
import se.dungeonsandmorons.model.dungeon.Dungeon;

public class DungeonService {

    public Dungeon findDungeonByName(String name) {
        if (!GameData.dungeons.isEmpty()) {
            for (Dungeon dungeon : GameData.dungeons) {
                if (dungeon.getName().equalsIgnoreCase(name)) {
                    return dungeon;
                }
            }
        }
        IO.println("Could not find specified dungeon.");
        return null;
    }

}
