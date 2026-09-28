package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public class Kharacter implements Combatant {
    protected String name;
    protected CharacterClass characterClass;
    protected int id;

    protected int level;
    private int experience;
    private int expPerLevel;

    private int maxHealth;
    private int currentHealth;

    private int maxMana;
    private int currentMana;

    private int unallocatedStatPoints;

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
    private final List<String> clearedDungeons = new ArrayList<>();

    public Kharacter(String name, CharacterClass characterClass) {
        this.name = name;
        this.characterClass = characterClass;
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

    Kharacter(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Kharacter(int id, String name, CharacterClass characterClass, int level, int experience, int unallocatedStatPoints, int strength, int agility, int intelligence, int vitality, int currentHealth, int maxHealth, int currentMana, int maxMana) {
        this.id = id;
        this.name = name;
        this.characterClass = characterClass;
        this.level = level;
        this.experience = experience;
        this.unallocatedStatPoints = unallocatedStatPoints;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.vitality = vitality;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.currentMana = currentMana;
        this.maxMana = maxMana;
    }

    public boolean allocateStat(Attribute attribute, int points) {
        if (points <= 0 || points > this.unallocatedStatPoints) {
            return false;
        }

        switch (attribute) {
            case STRENGTH       -> this.strength += points;
            case AGILITY        -> this.agility += points;
            case INTELLIGENCE   -> this.intelligence += points;
            case VITALITY       -> this.vitality += points;
        }

        this.unallocatedStatPoints -= points;
        return true;
    }

    // For Spring
    public void allocateStats (int str, int agi, int intel, int vit) {
        int total = str + agi + intel + vit;
        if (total <= 0) {
            throw new IllegalArgumentException(String.format(
                    "Cannot allocated %d points; only %d unallocated points available",
                    total, this.unallocatedStatPoints
            ));
        }

        // TODO: Calculate max health, etc..
    }

    public boolean isFirstTimeDungeon() {
        return firstTimeDungeon;
    }

    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    public void attack(Combatant attacker, Skill skill) {

    }

    public boolean takeDamage(int amount, Combatant attacker) {
        this.currentHealth = Math.max(0, this.currentHealth - amount);
        return isDead();
    }

    public void levelUp() {
        System.out.println(String.format("%s leveled up from level %d to level %d.", name, level, level+1));

        this.level++;
        this.unallocatedStatPoints += characterClass.getStatPointGain();

        this.currentHealth = maxHealth;
        this.currentMana = maxMana;

        updateStats();
    }

    private void updateStats() {
        maxHealth += (level * healthPerLevel) + (healthPerVitality * (vitality * vitalityPerLevel));
        maxMana += (level * manaPerLevel) + (manaPerIntelligence * (intelligence * intelligencePerLevel));
    }

    public void getStats() {
        System.out.print(String.format("""
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
                Unallocated Stat Points: %d
                """,
                name, characterClass, level, experience, maxHealth, maxMana, strength, agility, intelligence, vitality, unallocatedStatPoints));
    }

    public void addExperience(int amount) {
        if (this.experience + amount >= expPerLevel) {
            levelUp();
            this.experience = 0;
        } else {
            this.experience += amount;
            System.out.println(String.format("%s gained %d experience.", name, amount));
        }
    }

    public boolean learnSkill(Skill skill) {
        if (skill == null) {
            throw new NullPointerException("Trying to learn Null skill");
        }

        if (!skill.canBeLearnedBy(this.getCharacterClass())) {
            System.out.println(String.format("A %s cannot learn %s", this.getCharacterClass(), skill.name()));
            return false;
        }

        if (isSkillLearned(skill)) {
            System.out.println(String.format("%s already know this skill.", name));
            return false;
        }

        skills.add(skill);
        System.out.println(String.format("%s learned %s!", name, skill.name()));
        return true;
    }

    public boolean isSkillLearned(Skill skill) {
        if (getSkills() == null || getSkills().isEmpty()) {
            return false;
        }

        return skills.contains(skill);
    }

    public void setClearedDungeons(List<String> dungeons) {
        this.clearedDungeons.addAll(dungeons);
    }


    public CharacterClass getCharacterClass() { return characterClass; }
    public String getName() { return name; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxMana() { return maxMana; }
    public int getCurrentMana() { return currentMana; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getUnallocatedStatPoints() { return unallocatedStatPoints; }
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }
    public int getVitality() { return vitality; }
    public List<Skill> getSkills() { return List.copyOf(skills); }
    public List<String> getSkillNames() {
        return skills.stream()
                .map(Skill::name)
                .toList();
    }
    public List<String> getClearedDungeons() { return clearedDungeons; }

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
    public void addClearedDungeon(String dungeon) { this.clearedDungeons.add(dungeon); }

}
