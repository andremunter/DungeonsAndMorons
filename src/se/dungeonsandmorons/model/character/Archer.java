package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

public class Archer extends Character implements Targetable, Attacker {
    int damage = 3;

    public Archer(String name) {
        super(name);
    }

    @Override
    public void attack(Targetable target, Skill skill) {
        IO.println("Archer shot their bow and dealt " + calculateDamage(target) + " damage");
        target.takeDamage(calculateDamage(target));
    }

    @Override
    public void takeDamage(int damage) {
        setMaxHealth(getMaxHealth()-damage);
    }

    private int calculateDamage(Targetable target){
        int damage = getAgility() * 2;

        if (target instanceof Flyable){

            damage *= 2;
        }

        return damage;
    }
}
