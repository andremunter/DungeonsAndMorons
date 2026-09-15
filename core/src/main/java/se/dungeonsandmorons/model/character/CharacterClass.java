package se.dungeonsandmorons.model.character;

import org.w3c.dom.Attr;
import se.dungeonsandmorons.model.Attribute;

public enum CharacterClass {
    WARRIOR(
            Attribute.STRENGTH, // Primary Attribute, for damage scaling
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
    private final int unallocatedStats;
    private final int hpPerVitality;
    private final int manaPerIntelligence;


    CharacterClass(Attribute primaryAttribute, int unallocatedStats, int hpPerVitality, int manaPerIntelligence) {
        this.primaryAttribute = primaryAttribute;
        this.unallocatedStats = unallocatedStats;
        this.hpPerVitality = hpPerVitality;
        this.manaPerIntelligence = manaPerIntelligence;
    }

    public Attribute getPrimaryAttribute() { return primaryAttribute; }
    public int getUnallocatedStats() { return unallocatedStats; }
    public int getHpPerVitality() { return hpPerVitality; }
    public int getManaPerIntelligence() { return manaPerIntelligence; }
}
