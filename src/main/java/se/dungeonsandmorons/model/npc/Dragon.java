package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

public class Dragon extends Npc implements Attacker, Flyable {

    public Dragon(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        super(name, baseHealth, baseExp, level, rarity);
        this.skills.add(new Skill("Breathe fire", "Ffrsszh", 5, Attribute.NONE, 1, 0));
    }

    @Override
    public void attack(Targetable target, Skill skill) {
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
    public void fly() {
        IO.println("Dragon flew away");
    }


}
