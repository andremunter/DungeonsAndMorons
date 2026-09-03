package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.combat.Targetable;

public class Bird extends Npc implements Flyable {


    public Bird(String name, int health, int expOnDeath) {
        super(name, health, expOnDeath);
    }

    public void takeDamage(int amount) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        if (!isAlive()) {
            System.out.println(name + " died!");
        }
    }

    @Override
    public void fly() {

    }
}
