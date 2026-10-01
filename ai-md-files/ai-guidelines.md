# Dungeons and Morons AI guidance

## Project status

This repository contains a shared Java module (`:core`), a legacy terminal application (`:cli`), and an early Spring REST application (`:web`). There is no browser UI yet. The source code and tests are authoritative; these documents must distinguish current behavior from design proposals.

Implemented web routes currently cover saved-character listing, creation, retrieval, deletion and stat allocation, plus read-only dungeon queries. Combat is not wired to REST. `CombatEngine` exists in `:core`; `GameSession` and `PlayerSessionService` are early in-memory session work. `CombatService` is an empty work-in-progress stub and may be discarded.

## Architecture rules for new work

- Keep combat/domain rules separate from HTTP and presentation code. Translate HTTP requests and responses at the web boundary.
- `PlayerSessionService` is plain Java and is registered as a Spring bean in `:web`; the empty `CombatService` stub is not wired. Legacy core services/model methods still print to the console.
- The intended browser session scheme is server-side state addressed by a random opaque ID in an HttpOnly cookie. The ID is a sensitive bearer credential; the cookie must not carry game state or a self-contained access token. Use Secure over HTTPS, an intentional SameSite policy, appropriate path/lifetime, CSRF defenses for state-changing cookie-authenticated requests, and avoid logging IDs.
- The current session map is in-memory, process-local and has no expiry or HTTP cookie integration. Treat it as a prototype, not durable storage.
- Keep CLI-only terminal interaction in `:cli`; do not add new console I/O to REST flows or combat rules.

## Java and gameplay conventions

- Use the project's Java style and established types. Java records are already used for value objects such as `MonsterTemplate`, `MonsterAction`, and repository/web response records; do not rewrite mutable domain objects as records just for uniformity.
- Validate domain invariants and preserve existing exception patterns. `CombatEngine` rejects actions outside its expected state.
- Match actual registry content and formulas in `MonsterCatalog`, `SkillRegistry`, `Monster`, and `Kharacter`; do not rely on stale examples in design notes.
- Preserve the game's dry, bureaucratic fantasy satire in new dialogue and flavor text without obscuring mechanical outcomes.

## Documentation and delivery

- Label features as **implemented**, **work in progress**, or **proposed**.
- Do not present conceptual combat endpoints, browser views, DTOs, or unimplemented mechanics as available behavior.
- Never place database credentials or other secrets in source, docs, examples, fixtures, or output. Database settings come from `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
- Run the relevant existing Gradle test task for code changes; use `./gradlew test` for cross-module changes.
