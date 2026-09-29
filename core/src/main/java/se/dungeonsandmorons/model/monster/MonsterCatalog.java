package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.skill.SkillTag;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class MonsterCatalog {

    public static final MonsterTemplate DRAGON = new MonsterTemplate(
            "dragon",
            "Dragon",
            250,
            25,
            120,
            List.of(
                    new MonsterAction(
                            "Swipe",
                            20,
                            50,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Swipes with its razor-sharp claws."
                    ),
                    new MonsterAction(
                            "Fire Breath",
                            35,
                            35,
                            Set.of(SkillTag.RANGED, SkillTag.FIRE, SkillTag.SPELL),
                            "Expels a torrent of scorching flame."
                    ),
                    new MonsterAction(
                            "Incinerate",
                            50,
                            15,
                            Set.of(SkillTag.SPELL, SkillTag.FIRE),
                            "Focuses flame into a high-density thermal lance."
                    )
            )
    );

    public static final MonsterTemplate GOBLIN_SCOUT = new MonsterTemplate(
            "goblin_scout",
            "Goblin Scout",
            45,
            5,
            25,
            List.of(
                    new MonsterAction(
                            "Rusty Shank",
                            8,
                            70,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Stabs rapidly with a notched blade."
                    ),
                    new MonsterAction(
                            "Sling Pebble",
                            12,
                            30,
                            Set.of(SkillTag.RANGED, SkillTag.PHYSICAL),
                            "Hurls a jagged stone with a leather sling."
                    )
            )
    );


    private static final Map<String, MonsterTemplate> REGISTRY = new ConcurrentHashMap<>();

    static {
        register(DRAGON);
        register(GOBLIN_SCOUT);
    }

    private MonsterCatalog() {}

    public static void register(MonsterTemplate template) {
        REGISTRY.put(template.id(), template);
    }

    public static Optional<MonsterTemplate> findById(String id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }

    public static MonsterTemplate getRequired(String id) {
        return findById(id).orElseThrow(() ->
                new IllegalArgumentException("No monster template registered with id " + id));
    }

}
