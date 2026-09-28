package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.character.Combatant;
import se.dungeonsandmorons.model.skill.SkillTag;

import java.util.Set;

public class Dragon extends Monster {

    public Dragon(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        super(name, baseHealth, baseExp, level, rarity);


        actions.add(new MonsterAction(
                "Swipe", 20, 50,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                "Swipes with its claws"
        ));

        actions.add(new MonsterAction(
                "Fire Breath", 35, 35,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                "Breathes fire"
        ));

        actions.add(new MonsterAction(
                "Incinerate", 20, 15,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL),
                "Focuses flames from its belly into a super-heated laser-like fire"
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
