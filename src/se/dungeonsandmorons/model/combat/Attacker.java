package se.dungeonsandmorons.model.combat;

import se.dungeonsandmorons.model.skill.Skill;

public interface Attacker {
    void attack(Targetable target, Skill skill);
}
