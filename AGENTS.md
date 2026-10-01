# Dungeons and Morons - contributor guidance

## Project shape

- `:core` contains the shared Java game/domain code: characters, skills, monsters, dungeons, persistence interfaces/implementation, game services, and combat/session work.
- `:web` is the Spring Boot REST application. It currently exposes character-save/stat operations and read-only dungeon queries. Combat is not connected to its controllers, and there is no browser frontend in this repository yet.
- `:cli` is a separate terminal application with screens, input/output, and a game-state machine. It is still present and uses the shared core.
- `ai-md-files/` contains role and design references. Keep each document explicit about what exists today versus what is proposed. These files are guidance, not a substitute for source code.

## Build and run

- Run all existing tests: `./gradlew test`
- Run one module's tests: `./gradlew :core:test` or `./gradlew :web:test`
- Run the REST application: `./gradlew :web:bootRun`
- Configure database access with environment variables `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`. All are required; do not add credential defaults to source, configuration, examples, or tests.
- Gradle configures a Java 25 toolchain while compiling Java source for release 21. Follow the existing Gradle setup unless a task explicitly changes it.

## Architecture and change guidance

- Keep gameplay rules in the core domain and HTTP request/response concerns in `:web`. Do not make `CombatEngine` depend on Spring MVC, servlet APIs, or cookie types.
- The current `PlayerSessionService` is a plain Java class in `:core`, registered as a Spring bean by `:web`; `GameSession` holds a player plus optional dungeon/combat references. This is work toward web session orchestration, not a complete or durable session system.
- `CombatService` is an empty work-in-progress stub. Do not treat it as an established architecture or assume it must be used. Connect combat through an explicit orchestration boundary only after checking the existing `CombatEngine` and session APIs.
- The intended browser session design is a random, opaque session identifier in an `HttpOnly` cookie, with game/session state kept server-side. The ID is itself a sensitive bearer credential; do not put game state, player data, or a self-contained access token in the cookie. Set `Secure` in HTTPS deployments, an intentional `SameSite` policy, `Path=/`, and an appropriate lifetime; do not set a broad `Domain` unless required. Protect cookie-authenticated state-changing requests against CSRF (including validating request origin and/or a CSRF token), rotate or invalidate session IDs at appropriate lifecycle boundaries, and never log cookie values.
- The current in-memory session map is process-local and has no expiry or cookie integration. Do not describe it as persistent, horizontally scalable, or production-ready.
- Domain and service code currently includes some legacy console output. Keep framework wiring in `:web`; avoid adding new web/console coupling to combat and game rules.
- Preserve existing module conventions and validate behavior with the narrowest relevant Gradle test task, escalating to `./gradlew test` when changes cross modules.

## Source of truth

When implementation and design notes disagree, verify the Java source and tests, then update relevant `ai-md-files` documentation. Mark future UI, API contracts, mechanics, and architecture as proposed until implemented. Do not invent endpoints, DTOs, skills, classes, or game rules as existing features.

## Security and configuration

- Never commit credentials, tokens, session IDs, or other secrets. Read runtime secrets from environment/configuration and fail clearly when required settings are missing.
- Do not echo secrets into documentation, command output, test fixtures, or examples. Credentials exposed in repository history must be rotated at their provider; deleting literals from the working tree does not revoke them.
