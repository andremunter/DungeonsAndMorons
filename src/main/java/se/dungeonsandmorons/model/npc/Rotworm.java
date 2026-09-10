package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.combat.Digger;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.PlayerSkill;

public class Rotworm extends Enemy implements Attacker, Targetable, Digger {


    public Rotworm(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        super(name, baseHealth, baseExp, level, rarity);
        //this.skills.add(new Skill("Bite", "Slurp", 5, Attribute.NONE, 1, 0));
    }

    @Override
    public void attack(Targetable target, PlayerSkill skill) {
        if (!isAlive()) { return; }

        int skillDamage = skill.getBaseDamage();
        target.takeDamage(skillDamage, this);
        IO.println(String.format("%s used %s and dealt %d damage to %s(%d health left).", name, skill.getName(), skillDamage, target.getName(), target.getCurrentHealth()));
    }

    public void takeDamage(int amount, Attacker attacker) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        if (!isAlive()) {
            IO.println(name + " died!");
            if(attacker instanceof Character atker) {
                atker.addExperience(expOnDeath);
            }
        }
    }

    @Override
    public void dig(int turns) {
        IO.println(String.format("%s dug underground for %d turn%s.", name, turns, (turns>1) ? "s" : ""));
    }
}
