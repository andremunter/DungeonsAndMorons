package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Character;
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
