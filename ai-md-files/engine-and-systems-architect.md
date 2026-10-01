# Engine and systems architect

## Current architecture

- `:core` contains the game domain, repositories/services, `CombatEngine`, `GameSession`, and `PlayerSessionService`.
- `:web` owns the Spring Boot REST application and its controllers/DTOs. Combat currently has no REST controller or published combat DTO contract.
- `:cli` owns the terminal screens and state machine and remains an active source module.
- `PlayerSessionService` is plain Java and is registered in Spring by `:web`; the unfinished `CombatService` is not wired. Keep framework wiring in `:web` and avoid HTTP/servlet dependencies in combat/domain rules.

## Combat and session status

`CombatEngine` currently handles one player and one monster, supports `ATTACK`, `CAST`, and `RUN`, resolves a weighted monster action, records simple log strings, and moves between `CombatState` values. `HEAL` exists in `CombatAction` but is not implemented in the engine. Status effects, loot, and a complete encounter lifecycle are not implemented.

`GameSession` currently holds a `Kharacter`, optional current dungeon/combat references, and a last-active timestamp. `PlayerSessionService` loads a saved character into a `ConcurrentHashMap` keyed by a string session ID. There is no expiration, persistence, cookie, or REST integration yet.

`CombatService` is currently an empty, unwired stub with unused members. It is optional work in progress, not the required design. Evaluate whether a service is needed when wiring combat to the REST boundary; do not move HTTP concerns into `CombatEngine`.

## Intended browser session boundary

Use a cryptographically random opaque session ID in an `HttpOnly` cookie and keep player/game state server-side. The ID is itself a sensitive bearer credential; the cookie must not contain serialized game state or a self-contained access token, and JavaScript must not be able to read it. For browser requests:

- Set `Secure` on HTTPS, choose a deliberate `SameSite` policy, use `Path=/`, and choose a bounded lifetime; avoid an unnecessarily broad cookie domain.
- Protect state-changing requests against CSRF with appropriate SameSite behavior plus Origin validation and/or a CSRF token.
- Rotate/invalidate IDs at session creation, character changes, logout, and expiry as appropriate; never log cookie values.
- Enforce that a session is authorized for the character and game state it accesses. Do not trust a client-supplied character name or session ID as authorization by itself.
- Treat the current in-memory map as a single-process prototype. Define expiry and storage requirements before claiming restart persistence or horizontal scalability.

## Implementation guidance

Translate web input into domain commands and map domain outcomes to web DTOs in `:web`. Preserve state validation in the engine and make invalid transitions visible. Add tests around one action and the session boundary when those are implemented. Do not make the proposed API or UI in other guidance sound implemented.
