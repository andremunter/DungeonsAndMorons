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
        register("Densewood", () -> new Town(
                "Densewood",
                "Home to the King of Fools and its' equally unintelligent citizens.",
                List.of(
                        new Npc("Watchman Gourd", "Guard", "Halt! Who goes there? If you're a friend, say 'friend.' If you're a foe, say 'friend' anyway so I don't gotta draw this sword, 'cause I put it in backwards and the pointy bit is stuck in the scabbard."),
                        new Npc("Flint Blunt", "Blacksmith", "Hold on, don't interrupt the rhythm! Forty-two... forty-three... Damn rock won't bend today. Must be stubborn iron. You need armor, buy it now before I hit it again and make it worse"),
                        new Npc("Phil Phial", "Alchemist", "Careful! Do not breathe in the red fumes. Wait, what color is this? Hand me that looking-glass—no, wait, drink this first and tell me if your tongue goes numb. If it does, great news, it cures coughs!")
                )
        ));

        register("Cliffhanger", () -> new Town(
                "Cliffhanger",
                "A dizzying mountain village built literally over a sheer drop where the residents live in perpetual suspense.",
                List.of(
                        new Npc("Cliff Hanger", "Founder", "Im the founder of Cliffhanger, you can call me Cliff!"),
                        new Npc("Eileen Dover", "Alchemist", "Find all kinds of herbs & potions here!"),
                        new Npc("Brinkley", "Blacksmith", "I can get that blade right for you, or whip you up some fresh armor.")
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
