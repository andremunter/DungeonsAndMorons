package se.dungeonsandmorons.model.Npc;

import se.dungeonsandmorons.model.Attacker;
import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.Targetable;

public class Dragon extends Npc implements Targetable, Attacker, Flyable {

    @Override
    public void attack(Targetable target) {

    }

    @Override
    public void fly() {
        IO.println("Dragon flew away");
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp() - damage);
        IO.println("Dragon took " + damage + " damage, current hp = " + getHp());
    }

}
