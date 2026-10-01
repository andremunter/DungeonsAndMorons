# Implemented math and current combat flow

These notes describe the source as it exists now. Values below are not a promise that all balance is final.

## Monster scaling

`Monster` compounds template values from level 1 using the level offset `max(0, level - 1)`:

- Health growth: 12% per level.
- Action damage growth: 8% per level.
- Experience reward growth: 14% per level.
- Final health and experience apply the rarity multiplier and round to the nearest integer; health is clamped to at least 1.

```text
maxHealth = round(baseHealth * 1.12^levelOffset * rarityHealthMultiplier)
expOnDeath = round(baseExp * 1.14^levelOffset * rarityExperienceMultiplier)
actionDamage = round(baseActionDamage * 1.08^levelOffset * rarityDamageMultiplier)
```

Current `MonsterRarity` multipliers `(health, damage, experience)`:

| Rarity | HP | Damage | Experience |
|---|---:|---:|---:|
| NORMAL | 1.0 | 1.0 | 1.0 |
| MAGIC | 1.2 | 1.15 | 1.2 |
| RARE | 1.4 | 1.35 | 1.5 |
| EPIC | 1.8 | 1.6 | 1.8 |
| BOSS | 5.0 | 2.0 | 3.5 |

## Damage and mitigation

Player skill damage in `Kharacter` is `(baseDamage + attributeValue * attributeDamageMultiplier) * 1.08^levelOffset`, rounded. The unarmed fallback uses base damage 5 plus Strength. Monster damage uses the separate monster action formula above.

Both character and monster mitigation use `K = 100 + 15 * level` and `round(rawDamage * K / (K + max(0, armor)))`, with a minimum of 1 for a positive hit. `Monster.takeDamage` returns zero for zero damage or an already-dead monster and rejects negative damage. `Kharacter.takeDamage` does not currently perform those same input checks.

## Player progression and unfinished calculations

- A newly constructed `Kharacter` starts at level 0 with `expPerLevel = 45`. `addExperience` currently levels up at most once per call and resets experience to zero on level-up, so excess XP is discarded.
- `levelUp()` adds five unallocated points for the class, restores resources, then updates max resources using its current fixed growth constants and attribute-per-level fields.
- The level-curve formula and example tables in the root GDD are not implemented by `Kharacter`.
- `allocateStats(...)` is currently a TODO stub. Class HP/mana-per-stat fields are metadata and are not currently applied by `updateStats()`.

## Weighted monster actions

`Monster.chooseAction()` selects among configured actions in proportion to their positive integer weights using `ThreadLocalRandom`. Empty actions or non-positive total weight fail with `IllegalStateException`. Selection is nondeterministic; tests should not assume a specific action without introducing an injectable random source.

## Combat engine flow and limits

`CombatEngine` begins in `ROUND_START`; callers invoke `startRound()` to move to `AWAITING_PLAYER_INPUT`. The player can attack, cast, or run. If the monster survives an attack/cast, it selects and resolves one action. Surviving both advances the round and starts the next player input window. Victory, defeat and escape are terminal.

`HEAL` is present in `CombatAction` but is not implemented in `CombatEngine`. There are no implemented status effects, loot flow, or REST combat endpoints yet. Treat combat tuning tables, future actions and UI examples elsewhere as design proposals unless they match the source.
