package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.model.monster.MonsterAction;
import se.dungeonsandmorons.model.monster.MonsterCatalog;
import se.dungeonsandmorons.model.monster.MonsterRarity;
import se.dungeonsandmorons.model.skill.SkillTag;

import java.util.List;
import java.util.Set;

public final class DensewoodDungeons {

    private DensewoodDungeons() {}

    public static final String TOWN_ID = "densewood";

    public static void registerDungeons() {
        DungeonRegistry.register("densewood_outskirts", DensewoodDungeons::createDensewoodOutskirts);
        DungeonRegistry.register("densewood_fungal_hollow", DensewoodDungeons::createFungalHollow);
        DungeonRegistry.register("densewood_bramble_maze", DensewoodDungeons::createBrambleMaze);
        DungeonRegistry.register("densewood_whispering_depths", DensewoodDungeons::createWhisperingDepths);
        DungeonRegistry.register("densewood_heart_of_densewood", DensewoodDungeons::createHeartOfDensewood);
    }

    /**
     * Tier 1: Densewood Outskirts (Area Level 1)
     * Entry-level wilderness clearing along the edge of town, infested with scavengers.
     */
    public static Dungeon createDensewoodOutskirts() {
        Dungeon dungeon = Dungeon.builder()
                .id("densewood_outskirts")
                .name("Densewood Outskirts")
                .description("The sunlit perimeter where civilized roads give way to overgrown brush and opportunistic goblin raiders.")
                .townId(TOWN_ID)
                .areaLevel(1)
                .monsterRecipes(List.of(
                        MonsterCatalog.GOBLIN_SCOUT,
                        MonsterCatalog.GOBLIN_SCOUT,
                        MonsterCatalog.FOREST_WOLF
                ))
                .build();
        dungeon.spawnBoss(MonsterCatalog.GOBLIN_CHIEFTAIN);
        return dungeon;
    }

    /**
     * Tier 2: Fungal Hollow (Area Level 2)
     * Damp subterranean grove thick with toxic spores, glowing caps, and venomous weavers.
     */
    public static Dungeon createFungalHollow() {
        Dungeon dungeon = Dungeon.builder()
                .id("densewood_fungal_hollow")
                .name("The Fungal Hollow")
                .description("A moisture-choked cavern sunken beneath rotting roots, glowing faintly with sickly bioluminescent mold.")
                .townId(TOWN_ID)
                .areaLevel(2)
                .monsterRecipes(List.of(
                        MonsterCatalog.SPORE_SHRIEKER,
                        MonsterCatalog.GIANT_WEBSPINNER,
                        MonsterCatalog.FOREST_WOLF,
                        MonsterCatalog.SPORE_SHRIEKER
                ))
                .build();
        dungeon.spawnBoss(MonsterCatalog.BROOD_MATRON);
        return dungeon;
    }

    /**
     * Tier 3: Bramble Maze (Area Level 3)
     * Labyrinth of dense, thorny hedges repurposed as a fortified outpost by highwaymen.
     */
    public static Dungeon createBrambleMaze() {
        Dungeon dungeon = Dungeon.builder()
                .id("densewood_bramble_maze")
                .name("The Bramble Maze")
                .description("A fortified hedgerow maze where cutpurses and archers stage ambushes from the safety of impenetrable thornwalls.")
                .townId(TOWN_ID)
                .areaLevel(3)
                .monsterRecipes(List.of(
                        MonsterCatalog.BANDIT_CUTPURSE,
                        MonsterCatalog.BANDIT_BOWMAN,
                        MonsterCatalog.FOREST_WOLF,
                        MonsterCatalog.BANDIT_CUTPURSE
                ))
                .build();
        dungeon.spawnBoss(MonsterCatalog.BANDIT_CAPTAIN);
        return dungeon;
    }

    /**
     * Tier 4: Whispering Depths (Area Level 4)
     * Sunken megaliths where corrupted druidic rituals have tainted the woodland soil.
     */
    public static Dungeon createWhisperingDepths() {
        Dungeon dungeon = Dungeon.builder()
                .id("densewood_whispering_depths")
                .name("The Whispering Depths")
                .description("Submerged stone ruins where twisted vegetation and animated moss terrors heed the call of defiled druid wards.")
                .townId(TOWN_ID)
                .areaLevel(4)
                .monsterRecipes(List.of(
                        MonsterCatalog.CORRUPTED_DRUID,
                        MonsterCatalog.MOSS_GOLEM,
                        MonsterCatalog.SPORE_SHRIEKER,
                        MonsterCatalog.BRIAR_HORROR
                ))
                .build();
        dungeon.spawnBoss(MonsterCatalog.CORRUPTED_GROVEKEEPER);
        return dungeon;
    }

    /**
     * Tier 5: Heart of the Densewood (Area Level 5)
     * The climax encounter zone of Densewood, corrupted by an elder primordial entity.
     */
    public static Dungeon createHeartOfDensewood() {
        Dungeon dungeon = Dungeon.builder()
                .id("densewood_heart_of_densewood")
                .name("Heart of the Densewood")
                .description("The suffocating nucleus of the forest, where colossal roots crush ancient stone and primeval wrath awakens.")
                .townId(TOWN_ID)
                .areaLevel(5)
                .monsterRecipes(List.of(
                        MonsterCatalog.BRIAR_HORROR,
                        MonsterCatalog.MOSS_GOLEM,
                        MonsterCatalog.CORRUPTED_DRUID,
                        MonsterCatalog.BRIAR_HORROR
                ))
                .build();
        dungeon.spawnBoss(MonsterCatalog.ANCIENT_TREANT);
        return dungeon;
    }
}
