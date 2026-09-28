package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Kharacter;
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

    @Override
    public int takeDamage(int rawDamage, Combatant attacker) {
        int k = 100 + (15 * this.level);
        double mitigationFactor = (double) k / (k + Math.max(0, this.getArmor()));
        int finalDamage = Math.max(1, (int) Math.round(rawDamage * mitigationFactor));

        this.currentHealth = Math.max(0, this.currentHealth - finalDamage);

        if (!isAlive() && attacker instanceof Kharacter player) {
            player.addExperience(this.expOnDeath);
        }

        return finalDamage;
    }
}
