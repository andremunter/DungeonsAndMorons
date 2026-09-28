# Game Design Document: Dungeons and Morons 

---

## 1. Executive Summary & Core Philosophy

#### Dungeons and Morons is a browser-based, menu-driven RPG featuring turn-based combat, town exploration, and dungeon progression. The visual presentation centers entirely on structured web menus, status panels, and interactive buttons rather than 2D/3D graphical canvases. The game emphasizes snappy menu interaction, clear numerical scaling, and dry fantasy satire.

* **Genre:** Browser-Based Menu RPG / Incremental Dungeon Crawler
* **Interface:** Web GUI (Buttons, Panels, Modal Dialogs, Combat Log Feed)
* **Tone:** Parodic, dry, fantasy satire
* **Architecture:** Decoupled Architecture (Java Backend Engine + Web Client UI)

---

## 2. Core Game LoopThe gameplay loop cycles between safe urban nodes and hostile explorable areas:

Interactions are driven by asynchronous UI events and button clicks rather than terminal input prompts:
```
┌────────────────────────────────────────────────────────┐
│                      TOWN VIEW                         │
│  [Rest at Inn]  [Visit Blacksmith]  [Select Dungeon]   │
│  [Character Stats]   [View Skills]   [Save Game]       │
└───────────────────────────┬────────────────────────────┘
                            │ (Click Dungeon)
                            ▼
┌────────────────────────────────────────────────────────┐
│                    DUNGEON VIEW                        │
│   Area Level: 5  │  Dungeon: The Damp Cellar           │
│   [ Explore Area ]               [ Flee to Town ]      │
└───────────────────────────┬────────────────────────────┘
                            │ (Trigger Combat)
                            ▼
┌────────────────────────────────────────────────────────┐
│                    COMBAT VIEW                         │
│   Enemy: Lvl 5 Dragon               Player: HP 120/120 │
│   ┌──────────────────────────────────────────────────┐ │
│   │ Combat Log: Dragon used Swipe for 20 damage!     │ │
│   └──────────────────────────────────────────────────┘ │
│   [ Attack ]       [ Cast: Firebolt ]        [ Run ]   │
└────────────────────────────────────────────────────────┘
```

* **Town Hub View:** Players click dedicated action buttons to visit NPCs, review player stats, allocate skill points, or open the dungeon portal selector.
* **Dungeon Diving View:** Players click [Explore] to progress room counters and trigger encounters (monsters, treasure caches, or trap events).
* **Turn-Based Combat View:** Dedicated ability buttons enable the player to attack, cast spells, or flee. The backend evaluates actions, applies weighted enemy AI responses, and returns updated combat state DTOs to refresh the frontend UI
* **Resolution View:** Victory grants experience and loot modals; defeat triggers recovery sequences in town.

---

## 3. System & UI Architecture

### 3.1 Client Views (Frontend Panels)

* **Header / Status Bar:** Persistent top HUD displaying Player Level, HP bar, Currency/Gold, and current Location.
* **Town Hub:** Grid/list of interactive building cards or NPC dialogue buttons (`Talk`, `Trade`, `Upgrade`).
* **Dungeon View:** Encounter panel with progress bars, hazard status, and an **[Explore]** action trigger.
* **Combat Panel:** Turn tracker, visual HP/Mana bars, an auto-scrolling combat message log, and an action tray with cooldown/resource badges (`Attack`, `Cast`, `Run`).


### 3.2 Backend & State Management

* **Domain Engine:** The core Java modules (`CombatEngine`, `Monster`, `Kharacter`) remain pure domain models independent of web frameworks
* **State Machine:** Governs navigation (`TOWN`, `DUNGEON_EXPLORE`, `COMBAT`, `DIALOGUE`) and ensures combat actions can only be processed during valid states (`AWAITING_PLAYER_INPUT`).
* **DTO Serialization:** Endpoints consume player actions and produce light JSON payloads (e.g., `CombatRoundResultDto`, `TownStateDto`) containing updated entity health, state flags, and generated log strings.

---

## 4. Combat Mechanics & Calculations

### 4.1 Combat State Machine (`CombatEngine`)

The combat state machine dictates available UI actions:

* **`AWAITING_PLAYER_INPUT`:** Action buttons (`Attack`, `Cast`, `Run`) are enabled on the UI.
* **`RESOLVING_PLAYER_ACTION`:** Backend computes damage, status tag interactions, and triggers death checks.
* **`RESOLVING_ENEMY_ACTION`:** Enemy resolves an ability via weighted action roll and attacks the player.
* **`VICTORY` / `DEFEAT` / `ESCAPED`:** Disables combat trays and presents post-combat resolution modals.

## 4.2 Mathematical & Scaling Formulas

### 4.2.1 Core Growth Rates & Monster Stat Scaling
To eliminate linear stat dilution at higher levels, monster stats scale via discrete compound interest per level rather than flat additive factors:

* **HP Growth Rate ($g_{hp}$):** $0.12$ ($+12\%$ per level)
* **Damage Growth Rate ($g_{dmg}$):** $0.08$ ($+8\%$ per level)
* **EXP Reward Growth Rate ($g_{exp}$):** $0.14$ ($+14\%$ per level)

The effective level offset is clamped to zero for base-tier monsters:

$$\text{levelOffset} = \max(0, \text{level} - 1)$$

Raw scaled stats are calculated before applying rarity multipliers:

$$\text{levelHp} = \text{baseHealth} \times (1 + g_{hp})^{\text{levelOffset}}$$

$$\text{levelExp} = \text{baseExp} \times (1 + g_{exp})^{\text{levelOffset}}$$

Final monster health and experience reward values apply rarity-tier multipliers and round to the nearest whole integer:

$$\text{maxHealth} = \text{round}(\text{levelHp} \times M_{rarity, hp})$$

$$\text{expOnDeath} = \text{round}(\text{levelExp} \times M_{rarity, exp})$$

---

### 4.2.2 Action & Ability Damage Scaling
Damage is decoupled from `baseHealth` and calculated per ability using the monster's level and rarity. `MonsterAction` uses a `baseDamage` property rather than a static output:

$$\text{actionDamage} = \text{round}\left( \text{baseActionDamage} \times (1 + g_{dmg})^{\text{levelOffset}} \times M_{rarity, dmg} \right)$$

* **Level 1 Normal Dragon (`baseActionDamage` = 20):** deals $20$ damage.
* **Level 10 Normal Dragon ($M_{rarity, dmg} = 1.00$):** deals $20 \times (1.08)^9 \approx 40$ damage.
* **Level 10 Rare Dragon ($M_{rarity, dmg} = 1.35$):** deals $40 \times 1.35 = 54$ damage.

---

### 4.2.3 Monster Rarity Matrix & Time-To-Kill (TTK)
Rarity multipliers modify health pools, offensive threat, and player reward scaling to match intended encounter durations:

| Rarity Tier       | Health Multiplier ($M_{hp}$) | Damage Multiplier ($M_{dmg}$) | EXP Multiplier ($M_{exp}$) | Target TTK (Rounds) |
|:------------------|:-----------------------------|:------------------------------|:---------------------------|:--------------------|
| **NORMAL**        | 1.00                         | 1.00                          | 1.00                       | 3 – 4 rounds        |
| **MAGIC**         | 1.45                         | 1.15                          | 1.60                       | 4 – 6 rounds        |
| **RARE**          | 2.25                         | 1.35                          | 2.75                       | 7 – 9 rounds        |
| **BOSS / UNIQUE** | 4.50                         | 1.60                          | 6.00                       | 10 – 14 rounds      |

---

### 4.2.4 Armor & Damage Mitigation Curve
Incoming physical and skill damage is mitigated using an asymptotic diminishing-returns curve to prevent zero-damage locks and extreme spike damage:

$$\text{MitigationFactor} = \frac{K}{K + \text{Armor}}$$

$$\text{FinalDamage} = \max\left(1, \text{round}(\text{RawDamage} \times \text{MitigationFactor})\right)$$

The armor scaling constant $K$ scales with the target's level to prevent low-tier defense values from remaining overpowered in late-game zones:

$$K = 100 + (15 \times \text{DefenderLevel})$$

* A Level 1 player ($K = 115$) with **50 Armor** receives a $\approx 30.3\%$ damage reduction.
* A Level 10 player ($K = 250$) requires **108 Armor** to maintain that same $\approx 30.3\%$ damage reduction.

---

### 4.2.5 Player Level Progression Curve
Player leveling requirements scale superlinearly to keep early-game pacing rapid while stabilizing grind time in mid-to-late dungeons:

$$\text{ExpToNextLevel}(L) = \text{round}\left(120 \times L^{1.75}\right)$$

| Current Level ($L$) | EXP Needed for Next Level | Average Mob EXP (Base = 45) | Mobs to Level Up |
| :--- | :--- | :--- | :--- |
| **Level 1** | 120 | 45 | ~3 kills |
| **Level 5** | 2,014 | 82 | ~25 kills |
| **Level 10** | 6,734 | 157 | ~43 kills |
| **Level 20** | 22,668 | 582 | ~39 kills |

---

### 4.2.6 AI Action Selection (Roulette Wheel)
Monster turns evaluate action weights to select an ability probabilistically:

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