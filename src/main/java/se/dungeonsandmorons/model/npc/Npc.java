package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;


import java.util.ArrayList;
import java.util.List;

public abstract class Npc implements Targetable {
    protected final String name;
    private int maxHealth;
    protected int currentHealth;
    protected int expOnDeath;
    protected MonsterRarity rarity;

    protected List<Skill> skills = new ArrayList<>();



    Npc(String name, int maxHealth, int expOnDeath, MonsterRarity rarity) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.expOnDeath = expOnDeath;
        this.rarity = rarity;

    }

    private void applyRarityModifiers() {
        switch (rarity) {
            case MonsterRarity.MAGIC -> {
                maxHealth = (int) Math.round(maxHealth * RarityScaler.magic.health);
                expOnDeath = (int) Math.round(expOnDeath * RarityScaler.magic.experience);
            }
            case MonsterRarity.RARE -> maxHealth = (int) Math.round(maxHealth * RarityScaler.magic.health);
            case MonsterRarity.UNIQUE -> maxHealth = (int) Math.round(maxHealth * RarityScaler.magic.health);
            case MonsterRarity.BOSS -> maxHealth = (int) Math.round(maxHealth * RarityScaler.magic.health);

        }
    }


    public boolean isAlive() { return this.currentHealth > 0; }

    public String getName() { return this.name;}
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public int getCurrentHealth() { return this.currentHealth; }
    public List<Skill> getSkills() { return this.skills; }

    @Override
    public String toString() {
        return String.format("%s, %s", name, rarity);
    }

}
