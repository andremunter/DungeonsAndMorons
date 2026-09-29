# Mathematical Framework & Game Loop

## 1. Monster Scaling Formulas
All monster base statistics scale via discrete compound interest per level rather than additive factors:

- **HP Growth Rate ($g_{hp}$):** $0.12$ ($+12\%$ per level).
- **Damage Growth Rate ($g_{dmg}$):** $0.08$ ($+8\%$ per level).
- **EXP Reward Growth Rate ($g_{exp}$):** $0.14$ ($+14\%$ per level).

$$\text{levelOffset} = \max(0, \text{level} - 1)$$

$$\text{levelHp} = \text{baseHealth} \times (1 + g_{hp})^{\text{levelOffset}}$$

$$\text{levelExp} = \text{baseExp} \times (1 + g_{exp})^{\text{levelOffset}}$$

Final monster health and experience reward values apply rarity-tier multipliers and round to the nearest whole integer:

$$\text{maxHealth} = \text{round}(\text{levelHp} \times M_{rarity, hp})$$

$$\text{expOnDeath} = \text{round}(\text{levelExp} \times M_{rarity, exp})$$

### Monster Action Damage
Actions evaluate base damage against the monster's current level and rarity multiplier:

$$\text{actionDamage} = \text{round}\left( \text{baseActionDamage} \times (1 + g_{dmg})^{\text{levelOffset}} \times M_{rarity, dmg} \right)$$

```java
public int calculateActionDamage(MonsterAction action) {
    int levelOffset = Math.max(0, this.level - 1);
    double levelMultiplier = Math.pow(1.0 + 0.08, levelOffset);
    return (int) Math.round(action.getBaseDamage() * levelMultiplier * this.rarity.getDamageMultiplier());
}
```

---

## 2. Damage Mitigation & Armor Curve
All physical and skill damage passing through `Combatant.takeDamage(int rawDamage, Combatant attacker)` is mitigated using a hyperbolic diminishing-returns curve:

$$\text{MitigationFactor} = \frac{K}{K + \text{Armor}}$$

$$\text{FinalDamage} = \max\left(1, \text{round}(\text{RawDamage} \times \text{MitigationFactor})\right)$$

The defense scaling constant $K$ scales with defender level to keep armor ratings balanced across tiers:

$$K = 100 + (15 \times \text{DefenderLevel})$$

```java
@Override
public int takeDamage(int rawDamage, Combatant attacker) {
    int k = 100 + (15 * this.level);
    double mitigationFactor = (double) k / (k + Math.max(0, this.armor));
    int finalDamage = Math.max(1, (int) Math.round(rawDamage * mitigationFactor));

    this.currentHealth = Math.max(0, this.currentHealth - finalDamage);
    return finalDamage;
}
```

---

## 3. Player Stat & Damage Scaling
Player damage scaling combines skill base damage, primary attribute scaling, and level growth:

* **Unarmed Attack Base:** $5$ base damage, scales directly with Strength.
* **Skill Attack Base:** Determined by `skill.getBaseDamage()` and `skill.getScalingAttribute()`.
* **Level Compound Multiplier:** $(1.0 + 0.08)^{\text{levelOffset}}$.

```java
private int calculateRawDamage(Skill skill) {
    int levelOffset = Math.max(0, this.level - 1);
    double levelMultiplier = Math.pow(1.0 + 0.08, levelOffset);

    int baseDmg;
    int statScalingBonus;

    if (skill != null) {
        baseDmg = skill.getBaseDamage();
        int statValue = getAttributeValue(skill.getScalingAttribute());
        statScalingBonus = statValue * skill.getAttributeDamageMultiplier();
    } else {
        baseDmg = 5;
        statScalingBonus = this.strength;
    }

    int combinedBase = baseDmg + statScalingBonus;
    return (int) Math.round(combinedBase * levelMultiplier);
}
```

---

## 4. Monster Rarity Multipliers & TTK

| Rarity Tier       | Health Multiplier ($M_{hp}$) | Damage Multiplier ($M_{dmg}$) | EXP Multiplier ($M_{exp}$) | Target TTK (Rounds) |
|:------------------|:-----------------------------|:------------------------------|:---------------------------|:--------------------|
| **NORMAL**        | 1.00                         | 1.00                          | 1.00                       | 3 – 4 rounds        |
| **MAGIC**         | 1.45                         | 1.15                          | 1.60                       | 4 – 6 rounds        |
| **RARE**          | 2.25                         | 1.35                          | 2.75                       | 7 – 9 rounds        |
| **BOSS / UNIQUE** | 4.50                         | 1.60                          | 6.00                       | 10 – 14 rounds      |

---

## 5. Player Level Progression Curve
Player leveling requirements scale superlinearly to keep early-game pacing rapid while stabilizing grind time in mid-to-late dungeons:

$$\text{ExpToNextLevel}(L) = \text{round}\left(120 \times L^{1.75}\right)$$


| Current Level ($L$) | EXP Needed for Next Level | Average Mob EXP (Base = 45) | Mobs to Level Up |
|:--------------------|:--------------------------|:----------------------------|:-----------------|
| **Level 1**         | 120                       | 45                          | ~3 kills         |
| **Level 5**         | 2,014                     | 82                          | ~25 kills        |
| **Level 10**        | 6,734                     | 157                         | ~43 kills        |
| **Level 20**        | 22,668                    | 582                         | ~39 kills        |

---

## 6. AI Action Selection (Roulette Wheel)
Monsters select actions probabilistically based on assigned integer weights:

$$P(\text{Action}_i) = \frac{\text{weight}_i}{\sum_{j=1}^{n} \text{weight}_j}$$

```java
public MonsterAction chooseAction() {
    if (actions.isEmpty()) {
        throw new IllegalStateException(name + " has no actions configured!");
    }

    int totalWeight = actions.stream().mapToInt(MonsterAction::weight).sum();
    int roll = ThreadLocalRandom.current().nextInt(totalWeight);

    int cumulative = 0;
    for (MonsterAction action : actions) {
        cumulative += action.weight();
        if (roll < cumulative) {
            return action;
        }
    }
    return actions.getFirst();
}
```

---

## 7. Combat Turn Progression Sequence
Combat execution operates as a discrete finite state machine:

1. **`ROUND_START`:** Resolves active turn-start effects (regeneration, DoT ticks) before enabling the player input window.
2. **`AWAITING_PLAYER_INPUT`:** Player clicks an action button (`ATTACK`, `CAST`, `HEAL`, `RUN`).
3. **`RESOLVING_PLAYER_ACTION`:** Checks resource costs, calculates raw damage, invokes `target.takeDamage(rawDamage, this)`, and verifies target mortality. If target health drops to 0, transitions to `VICTORY`.
4. **`RESOLVING_ENEMY_ACTION`:** Enemy rolls a weighted ability via `chooseAction()`, calculates scaled damage, invokes `player.takeDamage(rawDamage, enemy)`, and checks player mortality. If player health drops to 0, transitions to `DEFEAT`.
5. **Round Step:** If both combatants survive, increments `roundCount` and resets state to `ROUND_START`.