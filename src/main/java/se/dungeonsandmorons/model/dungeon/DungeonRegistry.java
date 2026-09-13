package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.exception.CreateNullDungeonException;
import se.dungeonsandmorons.model.monster.Dragon;
import se.dungeonsandmorons.model.monster.Rotworm;

import java.util.*;
import java.util.function.Supplier;

public class DungeonRegistry {

    private static final Map<String, Supplier<Dungeon>> REGISTRY = new LinkedHashMap<>();

    static {
        register("Rotworm Cave", () -> {
            Dungeon rotwormCave = new Dungeon("Rotworm Cave", "Cave of only Rottenwurmen", 8);
            rotwormCave.populateDungeon(List.of(
                    (lvl, rarity) -> new Rotworm("Rotworm", lvl, 1, 20, rarity),
                    (lvl, rarity) -> new Rotworm("Rotworm", lvl, 1, 20, rarity)
            ));
            return rotwormCave;
        });

        register("Dragon Lair", () -> {
            Dungeon dragonLair = new Dungeon("Dragon Lair", "placeholder", 28);
            dragonLair.populateDungeon(List.of(
                    (lvl, rarity) -> new Dragon("Dragon Hatchling", lvl, 160, 40, rarity),
                    (lvl, rarity) -> new Dragon("Dragon Hatchling", lvl, 160, 40, rarity),
                    (lvl, rarity) -> new Dragon("Dragon",           lvl, 400, 120, rarity)
            ));
            return dragonLair;
        });
    }


    public static void register(String name, Supplier<Dungeon> factory) {
        REGISTRY.put(name.toLowerCase(), factory);
    }

    public static Dungeon create(String name) {
        Supplier<Dungeon> factory = REGISTRY.get(name.toLowerCase());
        if (factory == null) {
            throw new CreateNullDungeonException("Trying to create a null dungeon");
        }

        return factory.get();
    }

    public static List<Dungeon> createAll() {
        List<Dungeon> allDungeons = new ArrayList<>();
        for (Supplier<Dungeon> supplier : REGISTRY.values()) {
            allDungeons.add(supplier.get());
        }
        return allDungeons;
    }

    public static List<String> getAllDungeonNames() {
        return List.copyOf(REGISTRY.keySet());
    }


}
