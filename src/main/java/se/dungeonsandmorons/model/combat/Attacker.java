package se.dungeonsandmorons.model.combat;

import se.dungeonsandmorons.model.skill.PlayerSkill;

public interface Attacker {
    void attack(Targetable target, PlayerSkill skill);
}
