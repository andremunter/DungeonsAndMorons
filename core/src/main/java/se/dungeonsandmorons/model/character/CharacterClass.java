package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.Attribute;

public enum CharacterClass {
    WARRIOR(
            Attribute.STRENGTH, // Primary Attribute, for baseDamage scaling
            5,                  // Unallocated stat points per level
            6,                  // HP gained per vitality
            2                   // Mana gained per int

    ),
    ARCHER(
            Attribute.AGILITY,
            5,
            4,
            4
    ),
    MAGICIAN(
            Attribute.INTELLIGENCE,
            5,
            3,
            6
    );

    private final Attribute primaryAttribute;
    private final int statPointGain;
    private final int hpPerVitality;
    private final int manaPerIntelligence;


    CharacterClass(Attribute primaryAttribute, int statPointGain, int hpPerVitality, int manaPerIntelligence) {
        this.primaryAttribute = primaryAttribute;
        this.statPointGain = statPointGain;
        this.hpPerVitality = hpPerVitality;
        this.manaPerIntelligence = manaPerIntelligence;
    }

    public Attribute getPrimaryAttribute() { return primaryAttribute; }
    public int getUnallocatedStats() { return statPointGain; }
    public int getHpPerVitality() { return hpPerVitality; }
    public int getManaPerIntelligence() { return manaPerIntelligence; }
    public int getStatPointGain() { return statPointGain; }
}
