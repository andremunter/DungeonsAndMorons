package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.exception.CreateNullDungeonException;

import java.util.*;
import java.util.function.Supplier;

public class DungeonRegistry {

    private static final Map<String, Supplier<Dungeon>> REGISTRY = new LinkedHashMap<>();

    static {
        DensewoodDungeons.registerDungeons();
    }

    public static void register(String name, Supplier<Dungeon> factory) {
        REGISTRY.put(name.toLowerCase(), factory);
    }

    public static Dungeon create(String idOrName) {
        if (idOrName == null) {
            throw new CreateNullDungeonException("Cannot create dungeon from null identifier");
        }

        Supplier<Dungeon> factory = REGISTRY.get(idOrName.toLowerCase());
        if (factory == null) {
            // Fallback: search by display name match
            factory = REGISTRY.values().stream()
                    .filter(supplier -> supplier.get().getName().equalsIgnoreCase(idOrName))
                    .findFirst()
                    .orElse(null);
        }

        if (factory == null) {
            throw new CreateNullDungeonException("Trying to create a null dungoen: " + idOrName);
        }

        return factory.get();
    }

    public static List<Dungeon> createDungeonsByTownId(String townId) {
        if (townId == null) {
            return List.of();
        }

        return REGISTRY.values().stream()
                .map(Supplier::get)
                .filter(dungeon -> townId.equalsIgnoreCase(dungeon.getTownId()))
                .toList();
    }

    public static List<Dungeon> createAll() {
        return REGISTRY.values().stream()
                .map(Supplier::get)
                .toList();
    }

    public static List<String> getAllDungeonIds() {
        return List.copyOf(REGISTRY.keySet());
    }


}
