# UI View Specifications & Component Wireframes: Dungeons and Morons

This document specifies the screen layouts, interactive components, and visual state requirements for the browser client. All views are strictly menu-driven HTML/CSS interfaces without 2D/3D graphical canvases.

---

## 1. Global Layout Architecture

The screen layout consists of a persistent top HUD, a dynamic central viewport switching between game views, and modal overlays:

```
┌────────────────────────────────────────────────────────────────────────┐
│ [HUD] Hero Name | Lvl 5 Warrior | HP: [██████ 150/150] | MP: [███ 35/35]│
├────────────────────────────────────────────────────────────────────────┤
│                                                                        │
│                      DYNAMIC CENTRAL VIEWPORT                          │
│         (Town View  /  Dungeon View  /  Combat View)                   │
│                                                                        │
├────────────────────────────────────────────────────────────────────────┤
│ [PERSISTENT FOOTER / QUICK NAV]                                        │
│ [Character Sheet]    [Learned Skills]    [Inventory]    [Save Game]    │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Views Specification

### 2.1 Town View (`TownView`)
The default safe location hub.

* **Header Panel:** Displays active town name and town lore/status.
* **NPC Roster (Interactive Cards):**
    * Displays NPC Name and Role (e.g., "Flint Blunt - Blacksmith", "Old Mary - Innkeeper").
    * Action buttons: `[ Talk ]`, `[ Shop / Trade ]`.
* **Town Facilities:**
    * **Inn Card:** Button `[ Rest at Inn ]` to restore HP and Mana.
    * **Dungeon Gate Card:** Lists available dungeons linked to this town.
* **Dungeon Selector Modal:**
    * Displays dungeon cards with Name, Area Level, and a `[ Enter Dungeon ]` button.

---

### 2.2 Dungeon View (`DungeonView`)
Active when a player enters an explorable dungeon area.

* **Dungeon Status Header:**
    * Displays Dungeon Name and Area Level (e.g., "The Damp Cellar - Area Level 3").
    * Exploration progress bar (rooms cleared / encounters completed).
* **Main Exploration Panel:**
    * Event narrative box describing current surroundings or recent room triggers.
    * Primary Action Button: `[ Explore Room ]` (triggers `POST /api/dungeon/explore`).
    * Secondary Action Button: `[ Return to Town ]` (leaves dungeon safely).

---

### 2.3 Combat View (`CombatView`)
Active whenever an encounter triggers a battle.

```
┌────────────────────────────────────────────────────────────────────────┐
│ ENEMY PANEL: Dragon (Level 5) [RARE]                                   │
│ Health: [████████████████░░░░] 160 / 220 HP                            │
├────────────────────────────────────────────────────────────────────────┤
│ AUTO-SCROLLING COMBAT LOG                                              │
│ > Round 1: You attacked Dragon for 24 damage.                          │
│ > Round 1: Dragon used Fire Breath for 18 damage!                      │
├────────────────────────────────────────────────────────────────────────┤
│ PLAYER STATUS: You (Level 5 Warrior)                                   │
│ HP: [████████████████████] 150/150  │  MP: [██████████░░] 25/35        │
├────────────────────────────────────────────────────────────────────────┤
│ COMBAT ACTION TRAY                                                     │
│ [ Basic Attack ]   [ Skill: Strike (5 MP) ]   [ Skill: Shield (5 MP) ] │
│ [ Flee / Run ]                                                         │
└────────────────────────────────────────────────────────────────────────┘
```

* **Enemy Status Card:**
    * Enemy name, level, rarity badge (`NORMAL`, `MAGIC`, `RARE`, `BOSS`), and dynamic HP bar.
* **Combat Log Panel:**
    * Fixed-height container with `overflow-y: auto` displaying turn events.
    * Auto-scrolls to the newest message upon receiving state updates.
* **Player Action Tray:**
    * `[ Basic Attack ]`: Dispatches `POST /api/combat/action` with `{ "action": "ATTACK" }`.
    * `[ Skill Buttons ]`: Dynamic buttons generated from `availableSkills`. Displays skill name and MP cost; automatically greyed out/disabled if player MP is insufficient.
    * `[ Run ]`: Dispatches `{ "action": "RUN" }`.
* **Interactivity Constraint:**
    * All action buttons must be disabled while waiting for API responses and when `combatState !== 'AWAITING_PLAYER_INPUT'`.

---

## 3. Persistent Overlays & Modals

### 3.1 Character Sheet & Stat Allocation Modal
Triggered from the persistent HUD or footer.

* **Stats Displayed:**
    * Character Name, Class, Current Level, Current EXP / Needed EXP.
    * Attributes: Strength, Agility, Intelligence, Vitality.
    * Derived Stats: Max HP, Max Mana, Armor.
* **Stat Allocation Controls:**
    * Badge showing `Unallocated Stat Points`.
    * `[ + ]` increment buttons next to Strength, Agility, Intelligence, Vitality.
    * `[ Confirm Allocation ]` button sending updated allocation to the backend.

### 3.2 Terminal Battle Modal (`VICTORY` / `DEFEAT` / `ESCAPED`)
Overlays the screen when combat reaches a terminal state (`isTerminal: true`):

* **Victory Modal:**
    * Displays "VICTORY!", EXP gained, and loot drops.
    * Actions: `[ Continue Exploring ]`, `[ Return to Town ]`.
* **Defeat Modal:**
    * Displays "YOU WERE DEFEATED".
    * Shows death penalty message (e.g., loss of partial experience, waking up at town inn).
    * Action: `[ Respawn in Town ]`.
* **Escape Modal:**
    * Displays "ESCAPED SAFELY".
    * Action: `[ Back to Dungeon Entrance ]`.