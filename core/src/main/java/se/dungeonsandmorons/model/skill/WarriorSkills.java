package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;

import java.util.Set;

public final class WarriorSkills {

    private WarriorSkills() {}

    public static void registerSkills() {
        SkillRegistry.register(Skill.builder()
                .id("warrior_basic_melee_attack")
                .name("Swing")
                .description("")
                .baseDamage(5)
                .resourceCost(0)
                .scalingAttribute(Attribute.STRENGTH)
                .attributeDamageMultiplier(1.0)
                .targetType(TargetType.SINGLE_TARGET)
                .tags(Set.of(SkillTag.MELEE, SkillTag.PHYSICAL))
                .allowedClasses(Set.of(CharacterClass.WARRIOR))
                .build());

        SkillRegistry.register(Skill.builder()
                .id("warrior_strike")
                .name("Strike")
                .description("")
                .resourceCost(5)
                .baseDamage(10)
                .scalingAttribute(Attribute.STRENGTH)
                .attributeDamageMultiplier(1.08)
                .targetType(TargetType.SINGLE_TARGET)
                .tags(Set.of(SkillTag.MELEE, SkillTag.PHYSICAL))
                .allowedClasses(Set.of(CharacterClass.WARRIOR))
                .build());


    }

}
