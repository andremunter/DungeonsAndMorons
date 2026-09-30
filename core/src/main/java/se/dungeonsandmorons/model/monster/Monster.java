package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Combatant;
import se.dungeonsandmorons.model.dungeon.Dungeon;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Monster implements Combatant {
    private static final double HP_GROWTH_PER_LEVEL = 0.14;
    private static final double DMG_GROWTH_PER_LEVEL = 0.08;
    private static final double EXP_GROWTH_PER_LEVEL = 0.16;

    private final String templateId;
    private final String name;
    private int level;
    private MonsterRarity rarity;
    private int maxHealth;
    private int currentHealth;
    private int armor;
    private int expOnDeath;
    private List<MonsterAction> actions;

    public Monster(MonsterTemplate template, int level, MonsterRarity rarity) {
        Objects.requireNonNull(template, "MonsterTemplate cannot be null");
        Objects.requireNonNull(rarity, "MonsterRarity cannot be null");
        if (level < 1) {
            throw new IllegalArgumentException("Monster level must be at least 1, received: " + level);
        }

        this.templateId = template.id();
        this.level = level;
        this.rarity = rarity;
        this.actions = template.actions();
        this.armor = template.baseArmor();

        this.name = (rarity != MonsterRarity.NORMAL)
                ? rarity.name() + " " + template.name()
                : template.name();

        int levelOffset = Math.max(0, level - 1);

        double levelHp = template.baseHealth() * Math.pow(HP_GROWTH_PER_LEVEL, levelOffset);
        this.maxHealth = Math.max(1, (int) Math.round(levelHp * rarity.getHealthMultiplier()));
        this.currentHealth = this.maxHealth;

        double levelExp = template.baseExp() * Math.pow(EXP_GROWTH_PER_LEVEL, levelOffset);
        this.expOnDeath = Math.max(0, (int) Math.round(levelExp * rarity.getExperienceMultiplier()));
    }

    public Monster(MonsterTemplate template, int level) {
        this(template, level, MonsterRarity.NORMAL);
    }

    public MonsterAction chooseAction() {
        if (actions.isEmpty()) {
            throw new IllegalStateException("Monster " + this.name + " has no actions configured!");
        }

        int totalWeight = 0;
        for (MonsterAction action : this.actions) {
            totalWeight += action.weight();
        }

        if (totalWeight <= 0) {
            throw new IllegalStateException("Monster " + this.name + " has invalid total action weight: " + totalWeight);
        }

        int roll = ThreadLocalRandom.current().nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (MonsterAction action : actions) {
            cumulativeWeight += action.weight();
            if (roll < cumulativeWeight) {
                return action;
            }
        }

        return this.actions.getFirst();
    }

    public int calculateActionDamage(MonsterAction action) {
        int levelOffset = Math.max(0, this.level - 1);
        double levelMultiplier = Math.pow(1.0 + DMG_GROWTH_PER_LEVEL, levelOffset);
        double rarityMultiplier = this.rarity.getDamageMultiplier();

        return (int) Math.round(action.baseDamage() * levelMultiplier * rarityMultiplier);
    }

    @Override
    public int takeDamage(int rawDamage, Combatant attacker) {
        if (rawDamage < 0) {
            throw new IllegalStateException("Raw damage cannot be negative: " + rawDamage);
        }

        if (isDead() || rawDamage == 0) {
            return 0;
        }

        int k = 100 + (15 * this.level);
        double mitigationFactor = (double) k / (k + Math.max(0, this.getArmor()));
        int finalDamage = Math.max(1, (int) Math.round(rawDamage * mitigationFactor));

        this.currentHealth = Math.max(0, this.currentHealth - finalDamage);
        return finalDamage;
    }

    @Override
    public int getArmor() { return this.armor;}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getCurrentHealth() {
        return this.currentHealth;
    }

    @Override
    public int getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    @Override
    public boolean isDead() {
        return this.currentHealth <= 0;
    }

    public int getExpOnDeath() { return this.expOnDeath; }
    public MonsterRarity getRarity() { return this.rarity; }
    public List<MonsterAction> getActions() {
        return Collections.unmodifiableList(this.actions);
    }


    @Override
    public String toString() {
        return String.format("%s %s", rarity, name);
    }
}
