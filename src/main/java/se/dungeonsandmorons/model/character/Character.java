package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public abstract class Character implements Attacker, Targetable {
    protected String name;

    protected int level;
    private int experience;
    private int expPerLevel;

    private int maxHealth;
    private int currentHealth;

    private int maxMana;
    private int currentMana;

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

    private boolean firstTimeDungeon = true;

    protected List<Skill> skills = new ArrayList<>();

    Character(String name) {
        this.name = name;
        this.level = 0;
        this.experience = 0;
        this.expPerLevel = 45;
        maxHealth = 150;
        currentHealth = maxHealth;
        maxMana = 35;
        currentMana = maxMana;
        strength = 0;
        agility = 0;
        intelligence = 0;
        vitality = 0;
    }

    Character(String name, int level) {
        this.name = name;
        this.level = level;
    }

    Character(String name, int level, int experience, int strength, int agility, int intelligence, int vitality, int currentHealth, int maxHealth, int currentMana, int maxMana) {
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.vitality = vitality;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.currentMana = currentMana;
        this.maxMana = maxMana;
    }

    public boolean isFirstTimeDungeon() {
        return firstTimeDungeon;
    }

    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    public void takeDamage(int amount, Attacker attacker) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        if (!isAlive()) {
            IO.println(name + " died!");
        }
    }

    public void levelUp() {
        IO.println(String.format("%s leveled up from level %d to level %d.", name, level, level+1));

        addLevel(1);
        addStrength(strengthPerLevel);
        addAgility(agilityPerLevel);
        addIntelligence(intelligencePerLevel);
        addVitality(vitalityPerLevel);

        updateStats();
    }

    private void updateStats() {
        maxHealth += (level * healthPerLevel) + (healthPerVitality * (vitality * vitalityPerLevel));
        maxMana += (level * manaPerLevel) + (manaPerIntelligence * (intelligence * intelligencePerLevel)) ;

        currentHealth = maxHealth;
        currentMana = maxMana;

    }

    public void getStats() {
        IO.print(String.format("""
                Name: %s
                Class: %s
                Level: %d,
                Experience: %d
                MaxHealth: %d,
                MaxMana: %d,
                Strength: %d,
                Agility: %d,
                Intelligence: %d,
                Vitality: %d
                """, name, getClass().getSimpleName(), level, experience, maxHealth, maxMana, strength, agility, intelligence, vitality));
    }

    public void addExperience(int amount) {
        if (this.experience + amount >= expPerLevel) {
            levelUp();
            this.experience = 0;
        } else {
            this.experience += amount;
            IO.println(String.format("%s gained %d experience.", name, amount));
        }
    }

    public void learnSkill(Skill skill) {
        skills.add(skill);
    }

    void die() {

    }

    public String getName() { return this.name; }
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public int getCurrentHealth() { return this.currentHealth; }
    public int getMaxMana() { return this.maxMana; }
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
    public void setFirstTimeDungeon(boolean first) { this.firstTimeDungeon = first; }

    public void addLevel(int amount) { this.level += amount; }
    public void addMaxHealth(int amount) { this.maxHealth += amount; }
    public void addStrength(int amount) { this.strength += amount; }
    public void addAgility(int amount) { this.agility += amount; }
    public void addIntelligence(int amount) { this.intelligence += amount; }
    public void addVitality(int amount) { this.vitality += amount; }

}
