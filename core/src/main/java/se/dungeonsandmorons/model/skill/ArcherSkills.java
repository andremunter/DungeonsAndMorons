package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.character.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;

import java.util.Set;

public final class ArcherSkills {

    private ArcherSkills() {
        // Prevent instantiation
    }

    public static void registerSkills() {
        // Level 1: Standard resource-neutral attack
        SkillRegistry.register(Skill.builder()
                .id("archer_requisitioned_shot")
                .name("Requisitioned Shot")
                .description("Looses a standard-issue guild shaft. Aerodynamic failures and splintering remain the sole liability of the archer.")
                .resourceCost(0)
                .baseDamage(5)
                .scalingAttribute(Attribute.AGILITY)
                .attributeDamageMultiplier(1.00)
                .targetType(TargetType.SINGLE_TARGET)
                .tags(Set.of(SkillTag.RANGED, SkillTag.PHYSICAL))
                .allowedClasses(Set.of(CharacterClass.ARCHER))
                .build());

        // Level 1-2: Focused single-target mana spender
        SkillRegistry.register(Skill.builder()
                .id("archer_itemized_puncture")
                .name("Itemized Puncture")
                .description("Pauses to account for wind shear, target depreciation, and trajectory permits before firing a heavily scrutinized, high-impact arrow.")
                .resourceCost(5)
                .baseDamage(11)
                .scalingAttribute(Attribute.AGILITY)
                .attributeDamageMultiplier(1.10)
                .targetType(TargetType.SINGLE_TARGET)
                .tags(Set.of(SkillTag.RANGED, SkillTag.PHYSICAL))
                .allowedClasses(Set.of(CharacterClass.ARCHER))
                .build());

        // Level 2-3: Area-of-effect multi-target attack
        SkillRegistry.register(Skill.builder()
                .id("archer_blanket_notice")
                .name("Blanket Notice")
                .description("Discharges an indiscriminate spread of surplus arrows across the room, legally serving physical trauma to all eligible recipients.")
                .resourceCost(8)
                .baseDamage(6)
                .scalingAttribute(Attribute.AGILITY)
                .attributeDamageMultiplier(0.85)
                .targetType(TargetType.MULTIPLE_TARGETS)
                .tags(Set.of(SkillTag.RANGED, SkillTag.PHYSICAL))
                .allowedClasses(Set.of(CharacterClass.ARCHER))
                .build());
    }
}