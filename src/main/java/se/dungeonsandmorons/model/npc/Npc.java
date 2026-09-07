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
        this.maxHealth = rarity.scale(maxHealth, rarity.getHealthMultiplier());
        this.currentHealth = maxHealth;
        this.expOnDeath = rarity.scale(expOnDeath, rarity.getExperienceMultiplier());
        this.rarity = rarity;

    }

    public boolean isAlive() { return this.currentHealth > 0; }

    public String getName() { return this.name;}
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public int getCurrentHealth() { return this.currentHealth; }
    public int getDeathExperience() { return this.expOnDeath; }
    public MonsterRarity getRarity() { return this.rarity; }
    public List<Skill> getSkills() { return this.skills; }

    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public void setDeathExperience(int expOnDeath) { this.expOnDeath = expOnDeath; }

    @Override
    public String toString() {
        return String.format("%s, %s", name, rarity);
    }

}
