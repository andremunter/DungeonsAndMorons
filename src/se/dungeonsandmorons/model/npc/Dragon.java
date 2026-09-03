package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

public class Dragon extends Npc implements Attacker, Flyable {

    public Dragon(String name, int health, int expOnDeath) {
        super(name, health, expOnDeath);
        this.skills.add(new Skill("Breathe fire", "Ffrsszh", 5, Attribute.NONE, 1, 0));
    }

    @Override
    public void attack(Targetable target, Skill skill) {
        int skillDamage = skill.getBaseDamage();
        target.takeDamage(skillDamage);
        IO.println(String.format("%s used %s and dealt %d damage to %s(%d health left).", name, skill.getName(), skillDamage, target.getName(), target.getCurrentHealth()));
    }

    public void takeDamage(int amount) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        if (!isAlive()) {
            IO.println(name + " died!");
        }
    }

    @Override
    public void fly() {
        IO.println("Dragon flew away");
    }


}
