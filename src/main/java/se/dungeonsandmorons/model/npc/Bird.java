package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.Flyable;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.combat.Attacker;

public class Bird extends Npc implements Flyable {


    public Bird(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        super(name, baseHealth, baseExp, level, rarity);
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

    }
}
