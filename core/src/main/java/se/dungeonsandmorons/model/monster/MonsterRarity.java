package se.dungeonsandmorons.model.monster;

public enum MonsterRarity {
    NORMAL(1.0, 1.0, 1.0),
    MAGIC(1.2, 1.2, 1.2),
    RARE(1.4, 1.5, 1.5),
    EPIC(1.8, 2, 1.8),
    BOSS(5, 4, 3.5);

    private final double healthMultiplier;
    private final double damageMultiplier;
    private final double experienceMultiplier;

    MonsterRarity(double healthMultiplier, double damageMultiplier, double experienceMultiplier) {
        this.healthMultiplier      = healthMultiplier;
        this.damageMultiplier      = damageMultiplier;
        this.experienceMultiplier  = experienceMultiplier;
    }

    public double getHealthMultiplier()      { return healthMultiplier; }
    public double getDamageMultiplier()      { return damageMultiplier; }
    public double getExperienceMultiplier()  { return experienceMultiplier; }

    public int scale(int baseValue, double multiplier) {
        return (int) Math.round(baseValue * multiplier);
    }

}
