package se.dungeonsandmorons.town;

import se.dungeonsandmorons.exception.CreateNullTownException;
import se.dungeonsandmorons.model.npc.Npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TownRegistry {

    private static final Map<String, Supplier<Town>> REGISTRY = new LinkedHashMap<>();

    static {
        register("Foolstown", () -> new Town(
                "Foolstown",
                "Home to the King of Fools and its' equally unintelligent citizens.",
                List.of(
                        new Npc("Just Tim", "Guard", "They call me Just Tim."),
                        new Npc("Tim Hoddleston", "Alchemist", "Find all kinds of potions here!")
                )
        ));
    }

    public static void register(String name, Supplier<Town> supplier) {
        REGISTRY.put(name.toLowerCase(), supplier);
    }

    public static Town get(String name) {
        Supplier<Town> supplier = REGISTRY.get(name.toLowerCase());
        return supplier != null ? supplier.get() : null;
    }

}
