package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.Attacker;
import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.Targetable;

public class Warrior extends Character implements Targetable, Attacker {
    int damage = 3;

    public Warrior(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }

    @Override
    public void attack(Targetable target) {
        if (target instanceof Attacker){
            IO.println("Target can fight back");
        }
        int damageDealt = calculateDamage(target);
        IO.println("Warrior swung their axe and dealt " + damageDealt + " damage to " + target.getClass().getSimpleName());
        target.takeDamage(damageDealt);
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp() - damage);
    }

    private int calculateDamage(Targetable target){
        int damage = getStrength() * 2;

        if (target instanceof Flyable){
            IO.println("Målet flyger, du halvmissade yxkastet");
            damage /= 2;
        }

        return damage;
    }
}
