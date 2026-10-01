# Web API contracts: implementation status and proposals

There is no browser client in the repository. The endpoints below are the routes currently implemented by `:web`; examples in older documents for combat or dungeon exploration are proposals, not working contracts.

## Implemented routes

| Method | Route | Current behavior |
|---|---|---|
| `GET` | `/api/characters` | Lists character summaries. |
| `GET` | `/api/characters/{name}` | Loads a character save or returns 404. |
| `POST` | `/api/characters` | Creates a character save. |
| `DELETE` | `/api/characters/{name}` | Deletes a save or returns 404. |
| `PATCH` | `/api/characters/{name}/stats` | Calls stat allocation on a loaded save; the core allocation method is currently unfinished. |
| `GET` | `/api/dungeons` | Lists dungeon summaries. |
| `GET` | `/api/dungeons/{name}` | Returns one dungeon summary. |

Implemented Java DTOs are `CreateCharacterRequest`, `AllocateStatsRequest`, `ApiErrorResponse`, `DungeonResponse`, and `MonsterSummaryResponse`. Character endpoints currently address saves by character name; they are not backed by the new game-session cookie flow.

## Not implemented

There are no `/api/combat/*` or `/api/dungeon/explore` routes, no combat request/response DTOs, and no HTTP session cookie integration. Any TypeScript types or endpoint payloads added for those features must be labeled as a proposed contract until the Java DTOs, controller behavior, and tests exist. `HEAL` is not currently supported by `CombatEngine`.

## Intended browser session contract

For gameplay requests, use a random opaque server-side session ID carried only in an HttpOnly cookie. Game state remains on the server. The ID is a sensitive bearer credential; do not put character data, serialized combat state, or a self-contained access token in the cookie or browser storage. Set `Secure` in HTTPS deployments, a deliberate `SameSite` policy, `Path=/`, and an appropriate lifetime. Cookie-authenticated writes need CSRF protection, such as strict Origin checks and/or a CSRF token. Never log cookie values.

The current `PlayerSessionService` map is process-local and has no expiry or persistence; the eventual API must define session creation, character selection, expiry/logout, and invalid-session responses before claiming production-ready behavior.

## Contract design notes for future work

When combat endpoints are added, define Java DTOs from the actual `CombatEngine` and session operations; validate inputs and state on the server; return a complete, stable snapshot and structured log events rather than serializing mutable domain objects directly. Endpoint paths and fields are intentionally not fixed here.
