# Web UI & API Contracts: Dungeons and Morons

This document specifies the REST API endpoints, TypeScript interfaces, and JSON payloads for the frontend client.

---

## 1. REST Endpoints Overview

| Method   | Endpoint                | Description                                        | Request Body          | Response Body         |
|:---------|:------------------------|:---------------------------------------------------|:----------------------|:----------------------|
| **GET**  | `/api/combat/state`     | Retrieves the current combat session status        | None                  | `CombatStateResponse` |
| **POST** | `/api/combat/action`    | Dispatches a player combat action                  | `CombatActionRequest` | `CombatStateResponse` |
| **POST** | `/api/dungeon/explore`  | Steps dungeon exploration, triggering an encounter | None                  | `ExploreResultDto`    |
| **GET**  | `/api/character/status` | Fetches persistent character stats and resources   | None                  | `CharacterProfileDto` |

---

## 2. TypeScript Type Definitions

```typescript
// Combat action types matching backend enum se.dungeonsandmorons.combat.CombatAction
export type CombatAction = 'ATTACK' | 'CAST' | 'HEAL' | 'RUN';

// Combat FSM states matching backend enum se.dungeonsandmorons.combat.CombatState
export type CombatState = 
  | 'ROUND_START'
  | 'AWAITING_PLAYER_INPUT'
  | 'RESOLVING_PLAYER_ACTION'
  | 'RESOLVING_ENEMY_ACTION'
  | 'VICTORY'
  | 'DEFEAT'
  | 'ESCAPED';

export interface CombatantStatusDto {
  name: string;
  level: number;
  currentHp: number;
  maxHp: number;
  currentMana: number;
  maxMana: number;
  armor: number;
}

export interface CombatActionRequest {
  action: CombatAction;
  skillName?: string | null;
}

export interface CombatStateResponse {
  combatState: CombatState;
  roundNumber: number;
  player: CombatantStatusDto;
  enemy: CombatantStatusDto;
  availableSkills: string[];
  combatLog: string[];
  isTerminal: boolean;
}

export interface CharacterProfileDto {
  name: string;
  characterClass: 'WARRIOR' | 'ARCHER' | 'MAGICIAN';
  level: number;
  experience: number;
  unallocatedStatPoints: number;
  strength: number;
  agility: number;
  intelligence: number;
  vitality: number;
  currentHealth: number;
  maxHealth: number;
  currentMana: number;
  maxMana: number;
  armor: number;
  skills: string[];
}
```

---

## 3. Example JSON Payloads

### 3.1 Player Action Request (`POST /api/combat/action`)
```json
{
  "action": "CAST",
  "skillName": "Basic Magic Attack"
}
```

### 3.2 State Response (`CombatStateResponse`)
```json
{
  "combatState": "AWAITING_PLAYER_INPUT",
  "roundNumber": 2,
  "player": {
    "name": "MoronHero",
    "level": 3,
    "currentHp": 118,
    "maxHp": 150,
    "currentMana": 25,
    "maxMana": 35,
    "armor": 12
  },
  "enemy": {
    "name": "Dragon",
    "level": 3,
    "currentHp": 84,
    "maxHp": 120,
    "currentMana": 0,
    "maxMana": 0,
    "armor": 5
  },
  "availableSkills": [
    "Basic Magic Attack",
    "Strike"
  ],
  "combatLog": [
    "Round 1 begins!",
    "You used Basic Magic Attack on Dragon for 36 damage.",
    "Dragon used Swipe for 32 damage!"
  ],
  "isTerminal": false
}
```

### 3.3 Terminal Victory Response
```json
{
  "combatState": "VICTORY",
  "roundNumber": 3,
  "player": {
    "name": "MoronHero",
    "level": 3,
    "currentHp": 95,
    "maxHp": 150,
    "currentMana": 20,
    "maxMana": 35,
    "armor": 12
  },
  "enemy": {
    "name": "Dragon",
    "level": 3,
    "currentHp": 0,
    "maxHp": 120,
    "currentMana": 0,
    "maxMana": 0,
    "armor": 5
  },
  "availableSkills": [],
  "combatLog": [
    "You used Strike on Dragon for 42 damage.",
    "You defeated Dragon and earned 58 experience"
  ],
  "isTerminal": true
}
```

---

## 4. Frontend UI State & Rendering Rules

1. **Button Disabling & Locking:**
    - Action buttons (`Attack`, `Cast`, `Run`) must be disabled when `combatState !== 'AWAITING_PLAYER_INPUT'`.
    - If `isTerminal === true`, hide or lock the action tray and show the post-combat navigation modal (`Return to Town` / `Continue Dungeon`).
    - Disable any action button immediately upon user click while awaiting an asynchronous `fetch`/`axios` response.

2. **Resource Bars:**
    - HP Bar percentage: `Math.max(0, (currentHp / maxHp) * 100)%`
    - Mana Bar percentage: `Math.max(0, (currentMana / maxMana) * 100)%`
    - If `currentMana < skillCost`, the specific skill button must be styled as disabled.

3. **Auto-Scrolling Combat Log:**
    - Whenever `combatLog` updates, automatically scroll the container to bottom:
   ```javascript
   const logContainer = document.getElementById('combat-log');
   logContainer.scrollTop = logContainer.scrollHeight;
   ```