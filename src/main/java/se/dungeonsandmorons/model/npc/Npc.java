package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;


import java.util.ArrayList;
import java.util.List;

public abstract class Npc implements Targetable {
    private static final double HP_GROWTH_PER_LEVEL = 0.14;
    private static final double DMG_GROWTH_PER_LEVEL = 0.1;
    private static final double EXP_GROWTH_PER_LEVEL = 0.16;


    protected final String name;
    protected int level;
    private int maxHealth;
    protected int currentHealth;
    protected int expOnDeath;
    protected MonsterRarity rarity;

    protected List<Skill> skills = new ArrayList<>();

    Npc(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        this.name = (rarity != MonsterRarity.NORMAL ? rarity.name() + " " + name : name);
        this.rarity = rarity;

        int levelOffset = Math.max(0, level - 1);

        //double levelHp = baseHealth * (1.0 + (levelOffset))

        this.maxHealth = rarity.scale(maxHealth, rarity.getHealthMultiplier(), this);
        this.currentHealth = maxHealth;
        this.expOnDeath = rarity.scale(expOnDeath, rarity.getExperienceMultiplier(), this);


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
    public void setLevel(int level) { this.level = level; }

    @Override
    public String toString() {
        return String.format("%s", name);
    }

}
