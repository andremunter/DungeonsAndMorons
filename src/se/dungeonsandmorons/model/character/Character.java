package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public abstract class Character implements Attacker, Targetable {
    private String name;
    private final int baseHealth;
    private final int baseMana;

    private int maxHealth;
    private int currentHealth;

    private int maxMana;
    private int currentMana;

    private int level;
    private int experience;

    private int strength;
    private int agility;
    private int intelligence;
    private int vitality;

    private final int healthPerLevel = 3;
    private final int manaPerLevel = 2;
    private final int healthPerVitality = 5;
    private final int manaPerIntelligence = 4;

    protected int strengthPerLevel = 5;
    protected int agilityPerLevel = 3;
    protected int intelligencePerLevel = 1;
    protected int vitalityPerLevel = 3;

    protected List<Skill> skills = new ArrayList<>();

    Character(String name) {
        this.name = name;
        baseHealth = 150;
        baseMana = 35;
        maxHealth = baseHealth;
        currentHealth = maxHealth;
        maxMana = baseMana;
        currentMana = maxMana;
        strength = 5;
        agility = 5;
        intelligence = 5;
        vitality = 5;
    }

    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    public void takeDamage(int amount) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        if (!isAlive()) {
            IO.println(name + " died!");
        }
    }

    public void levelUp() {
        addLevel(1);
        addStrength(strengthPerLevel);
        addAgility(agilityPerLevel);
        addIntelligence(intelligencePerLevel);
        addVitality(vitalityPerLevel);

        updateStats();
    }

    private void updateStats() {
        maxHealth = (level * healthPerLevel) + (healthPerVitality * (vitality * vitalityPerLevel)) + baseHealth;
        maxMana = (level * manaPerLevel) + (manaPerIntelligence * (intelligence * intelligencePerLevel)) + baseMana;

        currentHealth = maxHealth;
        currentMana = maxMana;
    }

    void die() {

    }

    public String getName() { return this.name; }
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public int getCurrentHealth() { return this.currentHealth; }
    public int getCurrentMana() { return this.currentMana; }
    public int getLevel() { return this.level; }
    public int getExperience() { return this.experience; }
    public int getStrength() { return this.strength; }
    public int getAgility() { return this.agility; }
    public int getIntelligence() { return this.intelligence; }
    public int getVitality() { return this.vitality; }
    public List<Skill> getSkills() { return skills; }

    public void setMaxHealth(int health) { this.maxHealth = health; }
    public void setCurrentHealth(int health) { this.currentHealth = health; }
    public void setCurrentMana(int mana) { this.currentMana = mana; }
    public void setStrength(int strength) { this.strength = strength; }
    public void setAgility(int agility) { this.agility = agility; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }
    public void setVitality(int vitality) { this.vitality = vitality; }

    public void addLevel(int amount) { this.level += amount; }
    public void addExperience(int amount) { this.experience += amount; }
    public void addMaxHealth(int amount) { this.maxHealth += amount; }
    public void addStrength(int amount) { this.strength += amount; }
    public void addAgility(int amount) { this.agility += amount; }
    public void addIntelligence(int amount) { this.intelligence += amount; }
    public void addVitality(int amount) { this.vitality += amount; }

}
