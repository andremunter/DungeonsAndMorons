# Proposed browser UI views

This document is a product/UI proposal. There is no frontend source or implemented browser gameplay in the repository yet, and the proposed views are not proof that corresponding API endpoints exist.

## Presentation direction

Use a responsive, menu-driven interface with semantic HTML, clear focus/keyboard behavior, status panels, action buttons, and text logs. A graphical canvas/WebGL game is not the current product direction. Keep visual elements accessible and do not rely on color alone for health or disabled states.

## Proposed screens

- **Character selection/creation:** Use the existing character endpoints for save listing and creation. Do not imply authentication or per-player authorization is present.
- **Town:** Present location information and available actions only when backed by implemented game/session data. Town interaction, inventory, trading, rest, and save-game UI need APIs before being functional.
- **Dungeon:** The web app can currently query dungeon definitions. Dungeon exploration and encounter progress are not exposed by a REST endpoint.
- **Combat:** A combat panel may show player/enemy status, log entries, and action controls once combat is connected. Proposed controls might include attack, skill use, and flee; only attack/cast/run are currently represented by `CombatEngine`, and action support should be confirmed against implementation at integration time. Do not promise healing, cooldowns, loot or post-battle navigation before those mechanics exist.
- **Character sheet/stat allocation:** A stat-allocation endpoint exists, but core `Kharacter.allocateStats(...)` is unfinished. Treat functional allocation controls as blocked until the domain operation is implemented.

## Session handling

The browser should send the server-issued opaque session ID automatically in an HttpOnly cookie. JavaScript must not read or copy the cookie into local storage, request headers, logs, URLs, or analytics. Use CSRF defenses for cookie-authenticated mutations, handle expired/invalid sessions explicitly, and disable duplicate action submissions while a request is pending.

## Interaction principles

- Show request failures and stale-session states explicitly; do not silently display stale success.
- Disable actions while a request is pending and when the server says the action is invalid.
- Render combat logs as text, not trusted HTML. Auto-scroll only when the user is already near the bottom.
- Derive the final view and available actions from server responses, not client-side assumptions about game state.
