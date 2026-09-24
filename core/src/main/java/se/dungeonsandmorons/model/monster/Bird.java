package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.character.Combatant;

public class Bird extends Monster {

    public Bird(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        super(name, baseHealth, baseExp, level, rarity);
    }

    @Override
    public MonsterAction chooseAction() {
        return null;
    }

    public void takeDamage(int amount, Combatant attacker) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        if (!isAlive()) {
            IO.println(name + " died!");
            if(attacker instanceof Kharacter atker) {
                atker.addExperience(expOnDeath);
            }
        }
    }

}
