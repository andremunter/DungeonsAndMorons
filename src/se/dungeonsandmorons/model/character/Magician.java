package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

public class Magician extends Character implements Targetable, Attacker {

    public Magician(String name) {
        super(name);
    }

    @Override
    public void attack(Targetable target, Skill skill) {
        IO.println("Magician threw their wand and dealt " + calculateDamage() + " damage");
        target.takeDamage(calculateDamage());
    }

    @Override
    public void takeDamage(int damage) {
        setMaxHealth(getMaxHealth()-damage);
    }

    private int calculateDamage(){
        return getIntelligence() * 2;
    }

}
