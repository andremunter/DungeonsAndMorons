package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Combatant;
import se.dungeonsandmorons.model.skill.SkillTag;

import java.util.Set;

public class Rotworm extends Monster implements Combatant {


    public Rotworm(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        super(name, baseHealth, baseExp, level, rarity);

        actions.add(new MonsterAction(
                "Bite", 5, 100,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                "Bites"
        ));

    }

    public void takeDamage(int amount, Combatant attacker) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        if (!isAlive()) {
            IO.println(name + " died!");
            if(attacker instanceof Character atker) {
                atker.addExperience(expOnDeath);
            }
        }
    }
}
