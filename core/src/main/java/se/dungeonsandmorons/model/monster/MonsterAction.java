package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.combat.CombatAbility;
import se.dungeonsandmorons.model.skill.SkillTag;
import se.dungeonsandmorons.model.skill.TargetType;

import java.util.Set;

public record MonsterAction(
        String name,
        int baseDamage,
        int weight,
        Set<SkillTag> tags,
        String flavorText
) implements CombatAbility {

    @Override
    public TargetType getTargetType() {
        return TargetType.SINGLE_TARGET;
    }


}
