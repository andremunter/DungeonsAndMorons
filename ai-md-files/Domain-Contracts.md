# Current domain contracts

This file summarizes types that exist in `:core`. It is not an API schema; the REST module has separate DTOs under `web/.../web/dto`.

## Characters and combatants

- `model.character.Combatant` exposes name, current/max health, armor, `takeDamage(rawDamage, attacker)`, and default alive/dead checks.
- `model.character.Kharacter` is the mutable player model. It stores class, level/experience, attributes, health/mana, learned skills and cleared dungeon names. Its `allocateStats(...)` method is currently a TODO stub; use of stat allocation must not be assumed to update attributes yet.
- `CharacterClass` defines `WARRIOR`, `ARCHER`, and `MAGICIAN`, each with a primary attribute, five stat points per level, and HP/mana-per-stat metadata. Those metadata values do not currently drive `Kharacter.updateStats()`.

## Combat types

- `combat.CombatAbility` defines `name()`, `getTargetType()`, and `tags()`.
- `combat.CombatAction`: `ATTACK`, `CAST`, `HEAL`, `RUN`.
- `combat.CombatState`: `ROUND_START`, `AWAITING_PLAYER_INPUT`, `RESOLVING_PLAYER_ACTION`, `RESOLVING_ENEMY_ACTION`, `VICTORY`, `DEFEAT`, `ESCAPED`; terminal states are victory, defeat and escaped.
- `combat.CombatEvent` contains begin-turn, player/enemy attack, flee, and defeat events. It is not currently the driver of `CombatEngine`.
- `CombatEngine` receives a player and a monster, starts in `ROUND_START`, and requires `startRound()` before player action. It executes attack/cast/run, then resolves one weighted monster action. `HEAL` is not implemented. It exposes state, round count, and an unmodifiable combat log.
- `MonsterAction` is a record implementing `CombatAbility`, with name, base damage, positive selection weight, tags and flavor text. Target type is currently always single-target.

## Skills and monsters

- `Skill` implements `CombatAbility` and stores ID/name/description, resource cost, base damage, scaling attribute/multiplier, target type, tags and allowed classes. `SkillRegistry` currently registers the warrior and archer skill sets; no magician skill set is registered.
- `MonsterTemplate` is an immutable record/factory containing base health, armor, experience, and actions. `MonsterCatalog` currently registers templates including goblins, wolves, fungal creatures, bandits, woodland creatures and a dragon.
- `Monster` applies level and rarity scaling, chooses a monster action by positive integer weights, and implements damage mitigation.

## Dungeons, towns, saves, sessions

- `Dungeon` is mutable encounter state instantiated from factories in `DungeonRegistry`; `DensewoodDungeons` registers five Densewood dungeons. `TownRegistry` currently defines Densewood and Cliffhanger.
- `CharacterRepository` defines save/load/list/delete/existence operations. `CharacterSaveData` and `CharacterSummary` are repository value records; the PostgreSQL implementation is `PostgresCharacterRepository`.
- `GameSession` and `PlayerSessionService` are prototype session orchestration in `:core`. The service loads a save into an in-memory map by caller-provided string ID; no HTTP cookie integration, expiry, or durable game-session storage exists.

## REST contracts are separate

The currently implemented web DTOs are `CreateCharacterRequest`, `AllocateStatsRequest`, `ApiErrorResponse`, `DungeonResponse`, and `MonsterSummaryResponse`. Combat request/response DTO examples in older designs are not current Java types and must be treated as proposals until introduced and wired to controllers.
