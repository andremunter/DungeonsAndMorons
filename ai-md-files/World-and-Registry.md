# Current world and content registries

This catalog reflects the existing `:core` registrations. It is not a complete GDD; content and balance can change.

## Tone

The intended writing is dry, satirical fantasy: bureaucratic inconvenience and mundane concerns are treated seriously. Keep combat flavor text mechanically legible. Existing monster actions have short flavor strings, but do not assume every action in the project has polished narrative text.

## Character classes

`CharacterClass` currently defines:

| Class | Primary attribute | Stat-point gain | HP/vitality metadata | Mana/intelligence metadata |
|---|---|---:|---:|---:|
| WARRIOR | STRENGTH | 5 | 6 | 2 |
| ARCHER | AGILITY | 5 | 4 | 4 |
| MAGICIAN | INTELLIGENCE | 5 | 3 | 6 |

The HP/mana fields are metadata; `Kharacter.updateStats()` currently uses separate fixed constants and does not apply them. Newly constructed characters start at level 0. Do not describe class-specific resource growth as working until the core calculation changes.

## Registered skills

`SkillRegistry` registers `WarriorSkills` and `ArcherSkills`:

- Warrior: **Swing** (5 base damage, no resource cost, Strength, single target) and **Strike** (10 base damage, cost 5, Strength, single target).
- Archer: **Requisitioned Shot** (5 base damage, no cost, Agility, single target), **Itemized Puncture** (11 base damage, cost 5, Agility, single target), and **Blanket Notice** (6 base damage, cost 8, Agility, multiple targets).
- No Magician skill registration currently exists. Do not promise a registered magician spell set.

Each skill also declares scaling multiplier, tags, allowed classes, and a description in its Java builder.

## Monster registry and rarity

`MonsterCatalog` currently contains 15 templates: goblin scout/chieftain, forest wolf, spore shrieker, giant webspinner, brood matron, bandit cutpurse/bowman/captain, moss golem, corrupted druid/grovekeeper, briar horror, ancient treant, and dragon. Each template defines base health, armor, experience and weighted actions.

Current rarity multipliers `(HP, damage, EXP)` are NORMAL `(1.0, 1.0, 1.0)`, MAGIC `(1.2, 1.15, 1.2)`, RARE `(1.4, 1.35, 1.5)`, EPIC `(1.8, 1.6, 1.8)`, and BOSS `(5.0, 2.0, 3.5)`. These differ from earlier proposed balance tables.

## Towns and dungeons

- `TownRegistry` currently defines Densewood and Cliffhanger. Densewood has Watchman Gourd, Flint Blunt, and Phil Phial; Cliffhanger has Cliff Hanger, Eileen Dover, and Brinkley.
- `DensewoodDungeons` registers five dungeons, levels 1–5: Densewood Outskirts, The Fungal Hollow, The Bramble Maze, The Whispering Depths, and Heart of the Densewood. Each has a predefined list of normal monster factories and a boss factory.
- `Dungeon` instances are mutable runtime objects populated from those recipes. Registry lookups instantiate dungeons; no web exploration/session progression is currently implemented.
