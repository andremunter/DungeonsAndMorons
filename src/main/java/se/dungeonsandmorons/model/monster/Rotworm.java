package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Combatant;
import se.dungeonsandmorons.model.skill.MonsterAction;
import se.dungeonsandmorons.model.skill.PlayerSkill;

public class Rotworm extends Monster implements Combatant {


    public Rotworm(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        super(name, baseHealth, baseExp, level, rarity);
    }

    @Override
    public MonsterAction chooseAction() {
        return null;
    }

    public void attack(Combatant target, PlayerSkill skill) {
        if (!isAlive()) { return; }

        int skillDamage = skill.getBaseDamage();
        target.takeDamage(skillDamage, this);
        IO.println(String.format("%s used %s and dealt %d damage to %s(%d health left).", name, skill.getName(), skillDamage, target.getName(), target.getCurrentHealth()));
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
