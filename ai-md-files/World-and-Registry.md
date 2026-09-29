# World & Content Registry: Dungeons and Morons

This document catalogs existing character classes, registered skills, bestiary entries, locations, and narrative tone guidelines for *Dungeons and Morons*.

---

## 1. Tone & Narrative Style Guide

* **Genre Satire:** The world operates under mundane bureaucratic incompetence, inflated fantasy egos, and corporate-style dungeon management rather than epic, high-stakes drama.
* **Dry & Cynical Humor:** Characters and enemies take their absurd reality completely seriously (e.g., dragons worried about housing market valuations, blacksmiths refusing service over union lunch breaks).
* **Combat Flavor Text:** Every action must feature punchy, self-aware flavor text that grounds the ability in combat logs while maintaining mechanical clarity.

---

## 2. Character Classes & Baseline Attributes

All classes gain **5 unallocated stat points** per level, but differ in primary scaling attribute and derived resource yields:

| Class        | Primary Attribute        | HP Per Vitality | Mana Per Intelligence | Allowed Playstyle Focus                                        |
|:-------------|:-------------------------|:----------------|:----------------------|:---------------------------------------------------------------|
| **WARRIOR**  | `Attribute.STRENGTH`     | 6 HP            | 2 MP                  | Heavy physical melee, shields, and front-line survivability    |
| **ARCHER**   | `Attribute.AGILITY`      | 4 HP            | 4 MP                  | Ranged physical strikes, high evasion, and multi-hit abilities |
| **MAGICIAN** | `Attribute.INTELLIGENCE` | 3 HP            | 6 MP                  | High mana scaling, ranged elemental spells, and debuffs        |

---

## 3. Skill Catalog (`SkillRegistry`)

All skills implement `CombatAbility` and define resource costs, scaling attributes, and target classifications:

### 3.1 Warrior Skills
* **Basic Melee Attack**
    * **Base Damage:** 5
    * **Mana Cost:** 0
    * **Scaling:** `Attribute.STRENGTH` (Multiplier: 1x)
    * **Target Type:** `TargetType.SINGLE_TARGET`
    * **Tags:** `[SkillTag.MELEE, SkillTag.PHYSICAL]`
* **Strike**
    * **Base Damage:** 9
    * **Mana Cost:** 5
    * **Scaling:** `Attribute.STRENGTH` (Multiplier: 1x)
    * **Target Type:** `TargetType.SINGLE_TARGET`
    * **Tags:** `[SkillTag.MELEE, SkillTag.PHYSICAL]`
* **Raise Shield**
    * **Base Damage:** 0
    * **Mana Cost:** 5
    * **Scaling:** `Attribute.STRENGTH` (Multiplier: 1x)
    * **Target Type:** `TargetType.SELF`
    * **Tags:** `[SkillTag.MELEE, SkillTag.PHYSICAL]` (or `BUFF`/`DEFENSIVE`)

### 3.2 Magician Skills
* **Basic Magic Attack**
    * **Base Damage:** 4
    * **Mana Cost:** 0
    * **Scaling:** `Attribute.INTELLIGENCE` (Multiplier: 1x)
    * **Target Type:** `TargetType.SINGLE_TARGET`
    * **Tags:** `[SkillTag.RANGED, SkillTag.FIRE]`

---

## 4. Bestiary & Monster Design Roster

Monsters define base values, scaling rates, and a weighted action table for combat AI rolls:

### 4.1 Dragon (`se.dungeonsandmorons.model.monster.Dragon`)
* **Baseline Stats:** Base HP and Base EXP scale dynamically per level via compound growth.
* **Configured Actions:**
    1. **"Swipe"** (Base Damage: 20, Weight: 50)
        * **Tags:** `[MELEE, PHYSICAL]`
        * **Flavor Text:** *"Swipes with its claws"*
    2. **"Fire Breath"** (Base Damage: 35, Weight: 35)
        * **Tags:** `[MELEE, PHYSICAL]` (or `FIRE, SPELL`)
        * **Flavor Text:** *"Breathes fire"*
    3. **"Incinerate"** (Base Damage: 20, Weight: 15)
        * **Tags:** `[MELEE, PHYSICAL]` (or `FIRE, SPELL`)
        * **Flavor Text:** *"Focuses flames from its belly into a super-heated laser-like fire"*

---

## 5. World Locations & Dungeons

### 5.1 Towns (`TownScreen` / `TownView`)
* **Default Starting Town:** Hub containing persistent service NPCs.
* **NPCs:**
    * **Flint Blunt:** Town blacksmith providing equipment checks and repairs.

### 5.2 Dungeons (`ExploringDungeonScreen` / `DungeonView`)
* **The Damp Cellar:**
    * **Area Level:** 3–5
    * **Encounter Pool:** Small pests, low-level monstrosities, wandering dragons, and trapped dungeon caches.
    * **Exploration Flow:** Step-by-step exploration trigger rolling between monster combat, events, and safe returns to town.