package se.dungeonsandmorons.combat;

import se.dungeonsandmorons.model.skill.SkillTag;
import se.dungeonsandmorons.model.skill.TargetType;

import java.util.Set;

public interface CombatAbility {

    String getName();
    TargetType getTargetType();
    Set<SkillTag> getTags();

}
