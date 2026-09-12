package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.model.character.Combatant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public abstract class Monster implements Combatant {
    private static final double HP_GROWTH_PER_LEVEL = 0.14;
    private static final double DMG_GROWTH_PER_LEVEL = 0.1;
    private static final double EXP_GROWTH_PER_LEVEL = 0.16;

    protected final String name;
    protected int level;
    private int maxHealth;
    protected int currentHealth;
    protected int expOnDeath;
    protected MonsterRarity rarity;
    protected List<MonsterAction> actions = new ArrayList<>();

    Monster(String name, int level, int baseHealth, int baseExp, MonsterRarity rarity) {
        this.name = (rarity != MonsterRarity.NORMAL ? rarity.name() + " " + name : name);
        this.rarity = rarity;

        int levelOffset = Math.max(0, level - 1);

        double levelHp = baseHealth * (1.0 + (levelOffset * HP_GROWTH_PER_LEVEL));
        double levelExp = baseExp * (1.0 + (levelOffset * EXP_GROWTH_PER_LEVEL));

        // TODO: Should be calced when using skills
        double levelDmg = baseHealth * (1.0 + (levelOffset * DMG_GROWTH_PER_LEVEL));

        this.maxHealth = (int) Math.round(levelHp * rarity.getHealthMultiplier());
        this.expOnDeath = (int) Math.round(levelExp * rarity.getExperienceMultiplier());

        this.currentHealth = maxHealth;
    }

    public MonsterAction chooseAction() {
        if (actions.isEmpty()) {
            throw new IllegalStateException(name + " has no actions configured!");
        }

        int totalWeight = 0;
        for (MonsterAction action : actions) {
            totalWeight += action.weight();
        }

        int roll = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentThreshold = 0;
        for (MonsterAction action : actions) {
            currentThreshold += action.weight();
            if (roll < currentThreshold) {
                return action;
            }
        }

        return actions.getFirst();
    }

    public boolean isAlive() { return this.currentHealth > 0; }

    public String getName() { return this.name;}
    public int getMaxHealth(){ return this.maxHealth; }
    public int getCurrentHealth() { return this.currentHealth; }
    public int getDeathExperience() { return this.expOnDeath; }
    public MonsterRarity getRarity() { return this.rarity; }

    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public void setDeathExperience(int expOnDeath) { this.expOnDeath = expOnDeath; }
    public void setLevel(int level) { this.level = level; }

    @Override
    public String toString() {
        return String.format("%s %s", rarity, name);
    }

}
