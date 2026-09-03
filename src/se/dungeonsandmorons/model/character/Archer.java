package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.Attacker;
import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.Targetable;

public class Archer extends Character implements Targetable, Attacker {
    int damage = 3;

    public Archer(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }
    @Override
    public void attack(Targetable target) {
        IO.println("Archer shot their bow and dealt " + calculateDamage(target) + " damage");
        target.takeDamage(calculateDamage(target));
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp()-damage);
    }

    private int calculateDamage(Targetable target){
        int damage = getAgility() * 2;

        if (target instanceof Flyable){

            damage *= 2;
        }

        return damage;
    }
}
