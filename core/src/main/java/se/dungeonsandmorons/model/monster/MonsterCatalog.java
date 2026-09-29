package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.skill.SkillTag;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class MonsterCatalog {

    // --- Tier 1 Templates ---

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

    public static final MonsterTemplate FOREST_WOLF = new MonsterTemplate(
            "forest_wolf",
            "Forest Wolf",
            55,
            4,
            30,
            List.of(
                    new MonsterAction(
                            "Bite",
                            10,
                            65,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Snaps forward with savage jaws."
                    ),
                    new MonsterAction(
                            "Lunge",
                            15,
                            35,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Pounces violently, tearing at the flank."
                    )
            )
    );

    public static final MonsterTemplate GOBLIN_CHIEFTAIN = new MonsterTemplate(
            "goblin_chieftain",
            "Goblin Chieftain",
            110,
            12,
            90,
            List.of(
                    new MonsterAction(
                            "Cleaver Slam",
                            14,
                            55,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Brings down an oversized cleaver with crude brute force."
                    ),
                    new MonsterAction(
                            "War Cry",
                            8,
                            25,
                            Set.of(SkillTag.BUFF, SkillTag.PHYSICAL),
                            "Shrieks a deafening order that rattles composure."
                    ),
                    new MonsterAction(
                            "Bonebreaker",
                            20,
                            20,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "A heavy, devastating headbutt to the skull."
                    )
            )
    );

    // --- Tier 2 Templates ---

    public static final MonsterTemplate SPORE_SHRIEKER = new MonsterTemplate(
            "spore_shrieker",
            "Spore Shrieker",
            60,
            6,
            38,
            List.of(
                    new MonsterAction(
                            "Spore Burst",
                            11,
                            60,
                            Set.of(SkillTag.RANGED, SkillTag.SPELL),
                            "Detonates fungal pustules, spraying caustic powder."
                    ),
                    new MonsterAction(
                            "Toxic Mist",
                            16,
                            40,
                            Set.of(SkillTag.SPELL, SkillTag.DEBUFF),
                            "Exhales an acrid cloud that chokes lungs."
                    )
            )
    );

    public static final MonsterTemplate GIANT_WEBSPINNER = new MonsterTemplate(
            "giant_webspinner",
            "Giant Webspinner",
            80,
            8,
            50,
            List.of(
                    new MonsterAction(
                            "Venom Fangs",
                            13,
                            50,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Sinks dripping pedipalps into exposed flesh."
                    ),
                    new MonsterAction(
                            "Web Trap",
                            9,
                            30,
                            Set.of(SkillTag.RANGED, SkillTag.DEBUFF),
                            "Ejects sticky silk fibers to impede footing."
                    ),
                    new MonsterAction(
                            "Paralytic Bite",
                            18,
                            20,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Injects concentrated nerve toxins."
                    )
            )
    );

    public static final MonsterTemplate BROOD_MATRON = new MonsterTemplate(
            "brood_matron",
            "Brood Matron",
            150,
            15,
            130,
            List.of(
                    new MonsterAction(
                            "Mandible Crush",
                            18,
                            45,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Crushes armored plating between massive black shears."
                    ),
                    new MonsterAction(
                            "Acid Vomit",
                            24,
                            35,
                            Set.of(SkillTag.RANGED, SkillTag.SPELL),
                            "Spews corrosive stomach bile across the battle line."
                    ),
                    new MonsterAction(
                            "Brood Call",
                            30,
                            20,
                            Set.of(SkillTag.SPELL, SkillTag.PHYSICAL),
                            "Commands hatchlings to swarm in a frenzy."
                    )
            )
    );

    // --- Tier 3 Templates ---

    public static final MonsterTemplate BANDIT_CUTPURSE = new MonsterTemplate(
            "bandit_cutpurse",
            "Bandit Cutpurse",
            75,
            9,
            48,
            List.of(
                    new MonsterAction(
                            "Quick Thrust",
                            12,
                            65,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Darts forward with a pair of wicked stilettos."
                    ),
                    new MonsterAction(
                            "Flank Strike",
                            19,
                            35,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Slips behind armor seams for critical leverage."
                    )
            )
    );

    public static final MonsterTemplate BANDIT_BOWMAN = new MonsterTemplate(
            "bandit_bowman",
            "Bandit Bowman",
            65,
            6,
            45,
            List.of(
                    new MonsterAction(
                            "Aimed Shot",
                            15,
                            60,
                            Set.of(SkillTag.RANGED, SkillTag.PHYSICAL),
                            "Releases a fletched hunting shaft with precision."
                    ),
                    new MonsterAction(
                            "Barbed Arrow",
                            21,
                            40,
                            Set.of(SkillTag.RANGED, SkillTag.PHYSICAL),
                            "Fires an iron-hooked projectile designed to tear."
                    )
            )
    );

    public static final MonsterTemplate BANDIT_CAPTAIN = new MonsterTemplate(
            "bandit_captain",
            "Bandit Captain",
            180,
            18,
            160,
            List.of(
                    new MonsterAction(
                            "Broadsword Cleave",
                            19,
                            50,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Swings an executioner's steel broadsword in an arc."
                    ),
                    new MonsterAction(
                            "Shield Pummel",
                            14,
                            30,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Slams an iron-bossed buckler into the combatant's chest."
                    ),
                    new MonsterAction(
                            "Decapitate",
                            32,
                            20,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "A single execution strike aimed directly at the neck."
                    )
            )
    );

    // --- Tier 4 Templates ---

    public static final MonsterTemplate MOSS_GOLEM = new MonsterTemplate(
            "moss_golem",
            "Moss Golem",
            120,
            22,
            75,
            List.of(
                    new MonsterAction(
                            "Stone Fist",
                            17,
                            70,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Drives a granite knuckle cluster forward with dull finality."
                    ),
                    new MonsterAction(
                            "Seismic Tremor",
                            25,
                            30,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Slams both stone fists into the ground to split the bedrock."
                    )
            )
    );

    public static final MonsterTemplate CORRUPTED_DRUID = new MonsterTemplate(
            "corrupted_druid",
            "Corrupted Druid",
            95,
            10,
            70,
            List.of(
                    new MonsterAction(
                            "Thorn Volley",
                            16,
                            55,
                            Set.of(SkillTag.RANGED, SkillTag.SPELL),
                            "Channels necrotic energy into a barrage of hardened briars."
                    ),
                    new MonsterAction(
                            "Rotting Grasp",
                            22,
                            30,
                            Set.of(SkillTag.SPELL, SkillTag.DEBUFF),
                            "Summons fungal tendrils that sap vitality."
                    ),
                    new MonsterAction(
                            "Blood Blossom",
                            28,
                            15,
                            Set.of(SkillTag.SPELL, SkillTag.FIRE),
                            "Ignites botanical resin into an explosive burst."
                    )
            )
    );

    public static final MonsterTemplate CORRUPTED_GROVEKEEPER = new MonsterTemplate(
            "corrupted_grovekeeper",
            "Corrupted Grovekeeper",
            220,
            20,
            200,
            List.of(
                    new MonsterAction(
                            "Elder Stave",
                            20,
                            45,
                            Set.of(SkillTag.MELEE, SkillTag.SPELL),
                            "Strikes with an oak stave wrapped in dark pulsating runes."
                    ),
                    new MonsterAction(
                            "Briar Surge",
                            27,
                            35,
                            Set.of(SkillTag.SPELL, SkillTag.PHYSICAL),
                            "Calls jagged roots through the ground to skewer foes."
                    ),
                    new MonsterAction(
                            "Withering Hex",
                            36,
                            20,
                            Set.of(SkillTag.SPELL, SkillTag.DEBUFF),
                            "Channels an ancient ritual that corrodes physical defenses."
                    )
            )
    );

    // --- Tier 5 Templates ---

    public static final MonsterTemplate BRIAR_HORROR = new MonsterTemplate(
            "briar_horror",
            "Briar Horror",
            140,
            16,
            95,
            List.of(
                    new MonsterAction(
                            "Lashing Tendril",
                            20,
                            60,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Whips thorny vine segments with supersonic speed."
                    ),
                    new MonsterAction(
                            "Constrict",
                            27,
                            40,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Ensnared limbs are crushed inside a cage of thorns."
                    )
            )
    );

    public static final MonsterTemplate ANCIENT_TREANT = new MonsterTemplate(
            "ancient_treant",
            "Ancient Treant",
            280,
            28,
            240,
            List.of(
                    new MonsterAction(
                            "Timber Crush",
                            25,
                            45,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Brings down an ancient bough with devastating mass."
                    ),
                    new MonsterAction(
                            "Root Eruption",
                            34,
                            35,
                            Set.of(SkillTag.SPELL, SkillTag.PHYSICAL),
                            "Forces tectonic roots upward in a catastrophic burst."
                    ),
                    new MonsterAction(
                            "Cataclysmic Slam",
                            48,
                            20,
                            Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                            "Channels the primordial rage of the forest into a direct strike."
                    )
            )
    );

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

    private static final Map<String, MonsterTemplate> REGISTRY = new ConcurrentHashMap<>();

    static {
        register(GOBLIN_SCOUT);
        register(FOREST_WOLF);
        register(GOBLIN_CHIEFTAIN);
        register(SPORE_SHRIEKER);
        register(GIANT_WEBSPINNER);
        register(BROOD_MATRON);
        register(BANDIT_CUTPURSE);
        register(BANDIT_BOWMAN);
        register(BANDIT_CAPTAIN);
        register(MOSS_GOLEM);
        register(CORRUPTED_DRUID);
        register(CORRUPTED_GROVEKEEPER);
        register(BRIAR_HORROR);
        register(ANCIENT_TREANT);
        register(DRAGON);
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