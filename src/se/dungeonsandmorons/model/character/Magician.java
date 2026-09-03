package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.Attacker;
import se.dungeonsandmorons.model.Targetable;

public class Magician extends Character implements Targetable, Attacker {

    public Magician(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }

    @Override
    public void attack(Targetable target) {
        IO.println("Magician threw their wand and dealt " + calculateDamage() + " damage");
        target.takeDamage(calculateDamage());
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp()-damage);
    }

    private int calculateDamage(){
        return getIntelligence() * 2;
    }

}
