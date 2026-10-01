# Web and UI client engineer

## Project status

The repository currently has a Spring Boot REST API in `:web` and no frontend application. The API supports character save operations and read-only dungeon queries. There are no combat action/state routes or dungeon exploration routes yet. See `UI-API-Contracts.md` for current routes.

## Intended UI direction

Build a responsive, text-and-menu-driven browser RPG with semantic HTML and accessible controls. Do not use a canvas/WebGL game presentation. Keep UI views as proposals until client code and supporting server behavior exist.

## Browser session standard

Gameplay state is held server-side and selected by a random opaque session ID in an HttpOnly cookie. The client must not read it from JavaScript, put it in local storage, or send it through manually constructed headers or URLs. Set Secure over HTTPS and choose SameSite, path and lifetime deliberately. Use CSRF protection on state-changing cookie-authenticated requests, handle expiration/errors visibly, and never log cookie values.

The current `PlayerSessionService` is an in-memory Java map with no cookie integration, expiry, or durable storage. The client cannot use the session flow until the web layer exposes it.

## Client/API rules

- Treat implemented Java routes and DTOs as the contract; proposed combat payloads are not active endpoints.
- Derive available actions and current state from server responses. Do not assume frontend-only state changes are authoritative.
- Prevent duplicate submissions while a request is pending; show validation, conflict and expired-session responses clearly.
- Render logs and names as escaped text. Avoid injecting server-provided strings as HTML.
- Do not represent stat allocation, healing, loot, inventory, cooldowns, or NPC transactions as functional until server/domain support exists.
