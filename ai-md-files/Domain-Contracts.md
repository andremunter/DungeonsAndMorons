# Domain Contracts & Type System

## 1. Core Interfaces

### `Combatant.java`
```java
package se.dungeonsandmorons.model.character;

public interface Combatant {
    String getName();
    int getCurrentHealth();
    int getMaxHealth();

    default boolean isAlive() {
        return getCurrentHealth() > 0;
    }

    default boolean isDead() {
        return getCurrentHealth() <= 0;
    }

    /**
     * Mitigates raw damage via defender's armor and reduces current health.
     * @param rawDamage Unmitigated attack power from attacker
     * @param attacker The combatant dealing the damage
     * @return The actual damage dealt after mitigation
     */
    int takeDamage(int rawDamage, Combatant attacker);
}
```

### `CombatAbility.java`
```java
package se.dungeonsandmorons.combat;

import se.dungeonsandmorons.model.skill.SkillTag;
import se.dungeonsandmorons.model.skill.TargetType;

import java.util.Set;

public interface CombatAbility {
    String name();
    TargetType getTargetType();
    Set<SkillTag> tags();
}
```

---

## 2. Core Enums

### `Attribute.java`
```java
package se.dungeonsandmorons.model;

public enum Attribute {
    STRENGTH,
    AGILITY,
    INTELLIGENCE,
    VITALITY
}
```

### `CharacterClass.java`
```java
package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.Attribute;

public enum CharacterClass {
    WARRIOR(Attribute.STRENGTH, 5, 6, 2),
    ARCHER(Attribute.AGILITY, 5, 4, 4),
    MAGICIAN(Attribute.INTELLIGENCE, 5, 3, 6);

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
    public int getStatPointGain() { return statPointGain; }
    public int getUnallocatedStats() { return statPointGain; }
    public int getHpPerVitality() { return hpPerVitality; }
    public int getManaPerIntelligence() { return manaPerIntelligence; }
}
```

### `CombatAction.java`
```java
package se.dungeonsandmorons.combat;

public enum CombatAction {
    ATTACK,
    CAST,
    HEAL,
    RUN
}
```

### `CombatState.java`
```java
package se.dungeonsandmorons.combat;

public enum CombatState {
    ROUND_START,
    AWAITING_PLAYER_INPUT,
    RESOLVING_PLAYER_ACTION,
    RESOLVING_ENEMY_ACTION,
    VICTORY,
    DEFEAT,
    ESCAPED;

    public boolean isTerminal() {
        return this == VICTORY || this == DEFEAT || this == ESCAPED;
    }
}
```

### `CombatEvent.java`
```java
package se.dungeonsandmorons.combat;

public enum CombatEvent {
    BEGIN_TURN,
    PLAYER_ATTACKED,
    PLAYER_FLED,
    ENEMY_DEFEATED,
    ENEMY_ATTACKED,
    PLAYER_DEFEATED
}
```

### `TargetType.java`
```java
package se.dungeonsandmorons.model.skill;

public enum TargetType {
    SINGLE_TARGET,
    MULTIPLE_TARGETS,
    SELF,
    SELF_AND_TARGET,
    SELF_AND_TARGETS
}
```

### `SkillTag.java`
```java
package se.dungeonsandmorons.model.skill;

public enum SkillTag {
    MELEE,
    RANGED,
    SPELL,
    PHYSICAL,
    FIRE,
    COLD,
    LIGHTNING,
    HEAL,
    BUFF,
    DEBUFF
}
```

### `MonsterRarity.java`
```java
package se.dungeonsandmorons.model.monster;

public enum MonsterRarity {
    NORMAL(1.0, 1.0, 1.0),
    MAGIC(1.45, 1.15, 1.6),
    RARE(2.25, 1.35, 2.75),
    BOSS(4.5, 1.6, 6.0);

    private final double healthMultiplier;
    private final double damageMultiplier;
    private final double experienceMultiplier;

    MonsterRarity(double healthMultiplier, double damageMultiplier, double experienceMultiplier) {
        this.healthMultiplier = healthMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.experienceMultiplier = experienceMultiplier;
    }

    public double getHealthMultiplier() { return healthMultiplier; }
    public double getDamageMultiplier() { return damageMultiplier; }
    public double getExperienceMultiplier() { return experienceMultiplier; }
}
```

---

## 3. Web API Transfer Contracts (DTOs)

### `CombatActionRequest.java`
```java
package se.dungeonsandmorons.api.dto;

import se.dungeonsandmorons.combat.CombatAction;

public record CombatActionRequest(
    CombatAction action,
    String skillName
) {}
```

### `CombatantStatusDto.java`
```java
package se.dungeonsandmorons.api.dto;

public record CombatantStatusDto(
    String name,
    int level,
    int currentHp,
    int maxHp,
    int currentMana,
    int maxMana,
    int armor
) {}
```

### `CombatStateResponse.java`
```java
package se.dungeonsandmorons.api.dto;

import java.util.List;

public record CombatStateResponse(
    String combatState,
    int roundNumber,
    CombatantStatusDto player,
    CombatantStatusDto enemy,
    List<String> availableSkills,
    List<String> combatLog,
    boolean isTerminal
) {}
```