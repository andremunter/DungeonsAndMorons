
// --- Global Game State ---
const state = {
    activeCharacter: null
};

// --- Screen Switching ---
function showScreen(screenId) {
    document.getElementById("screen-select").style.display = "none";
    document.getElementById("screen-town").style.display = "none";
    document.getElementById("screen-dungeon-gate").style.display = "none";
    document.getElementById("screen-dungeon-run").style.display = "none";
    document.getElementById("screen-combat").style.display = "none";

    const activeScreen = document.getElementById(screenId);
    if (activeScreen) {
        activeScreen.style.display = "block";
    }
}


// --- API Functions ---

async function leaveDungeon() {
    const res = await fetch("/api/dungeons/leave", {
        method: "POST"
    });

    if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg || "Failed to leave dungeon");
    }
    return await res.text();
}

async function fetchDungeons() {
    const res = await fetch("/api/dungeons");
    if (!res.ok) throw new Error("Failed to load available dungeons");
    return await res.json();
}

async function enterDungeon(dungeonName) {
    // Replace spaces with hyphens to match the controller's path variable formatting
    const formattedName = encodeURIComponent(dungeonName.replace(/\s+/g, "-"));
    const res = await fetch (`/api/dungeons/${formattedName}/enter`, {
        method: "POST"
    });

    if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg || "Failed to enter dungeon");
    }
    return await res.text();
}

async function explore() {
    const res = await fetch("/api/dungeons/explore", {
        method: "POST"
    });

    if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg || "Failed to explore dungeon");
    }
    return await res.json();
}

async function sendCombatAction(action, skillName = null) {
    const res = await fetch("/api/combat/action", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ action, skillName })
    });

    if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg || "Failed to send combat action");
    }
    return await res.json();
}

async function fetchSavedCharacters() {
    const res = await fetch("/api/characters");
    if (!res.ok) throw new Error("Failed to load characters");
    return await res.json();
}

async function selectCharacter(name) {
    const res = await fetch(`/api/characters/${encodeURIComponent(name)}/select`, {
        method: "POST"
    });

    if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg || "Failed to select character");
    }
    return await res.json();
}

async function createCharacter(name, characterClass) {
    const res = await fetch("/api/characters", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({name, characterClass })
    });

    if (!res.ok) {
       try {
           const errJson = await res.json();
           throw new Error(errJson.message || errJson.error || "Failed to create character");
       } catch (e) {
           if (e.message && e.message !== "Failed to create character") throw e;
           const errorText = await res.text();
           throw new Error(errorText || "Failed to create character");
       }
    }
    return await res.json();
}

// --- UI Update Functions ---

async function loadDungeonGateScreen() {
    showScreen("screen-dungeon-gate");
    const listContainer = document.getElementById("dungeon-list");
    const errorBox = document.getElementById("dungeon-gate-error");
    errorBox.textContent = "";
    listContainer.innerHTML = "Loading expeditions...";

    try {
        const dungeons = await fetchDungeons();
        listContainer.innerHTML = "";

        if (dungeons.length === 0) {
            listContainer.innerHTML = "<p>No dungeons available right now</p>";
            return
        }

        dungeons.forEach(dungeon => {
            const card = document.createElement("div");
            card.className = "character-card";
            card.style.marginBottom = "0.75rem";
            card.innerHTML = `
                <div>
                    <strong>${dungeon.name}</strong> (Level ${dungeon.areaLevel})
                    <div style="font-size: 0.85em; color: #aaa; margin: 0.25rem 0;">
                        ${dungeon.description}
                    </div>
                    <div style="font-size: 0.8em; color: #888;">
                        Encounters ${dungeon.monsterCount} monsters
                    </div>
                </div>
                <button onclick="handleEnterDungeon('${dungeon.name}')">Enter</button>
            `;
            listContainer.appendChild(card);
        });
    } catch (err) {
        listContainer.innerHTML = "";
        errorBox.textContent = err.message;
    }
}

async function loadCharacterSelectScreen() {
    const listContainer = document.getElementById("character-list");
    const errorBox = document.getElementById("select-error");
    errorBox.textContent = "";

    try {
        const characters = await fetchSavedCharacters();
        listContainer.innerHTML = "";
        if (characters.length === 0) {
            listContainer.innerHTML = "<p>No characters found. Create one below!.</p>";
            return;
        }

        characters.forEach(char => {
            const card = document.createElement("div");
            card.className = "character-card";
            card.innerHTML = `
                <div>
                    <strong>${char.name}</strong> - Level ${char.level} ${char.characterClass}
                    <div style="font-size: 0.85em; color: #888;">Location: ${char.lastTownVisited}</div>
                </div>
                <button onclick="handleSelectCharacter('${char.name}')">Select</button>
            `;
            listContainer.appendChild(card);
        });
    } catch (err) {
        listContainer.innerHTML = "";
        errorBox.textContent = err.message;
    }
}

// --- Event Handlers ---

async function handleEnterDungeon(dungeonName) {
    const errorBox = document.getElementById("dungeon-gate-error");
    errorBox.textContent = "";

    try {
        await enterDungeon(dungeonName);
        startDungeonRunView(dungeonName);
    } catch (err) {
        errorBox.textContent = err.message;
    }
}

function startDungeonRunView(dungeonName) {
    document.getElementById("active-dungeon-title").textContent = `Exploring: ${dungeonName}`;
    document.getElementById("dungeon-event-text").textContent = "You step cautiously into the depths...";
    document.getElementById("btn-explore-room").disabled = false;
    showScreen("screen-dungeon-run");
}

async function handleExplore() {
    const eventText = document.getElementById("dungeon-event-text");
    const errorBox = document.getElementById("dungeon-run-error");
    errorBox.textContent = "";

    try {
        const response = await explore(); // calls POST /api/dungeons/explore
        eventText.textContent = response.message;

        if (response.eventType === "COMBAT") {
            startCombatView(response.combatState);
        } else if (response.eventType === "CLEARED") {
            document.getElementById("btn-explore-room").disabled = true;
            eventText.textContent += " Head for the exit to secure your rewards!";
        }
    } catch (err) {
        errorBox.textContent = err.message;
    }
}

async function handleLeave() {
    const errorBox = document.getElementById("dungeon-run-error");
    errorBox.textContent = "";

    try {
        const summary = await leaveDungeon();
        alert(summary);

        if (state.activeCharacter) {
            enterTown(state.activeCharacter);
        } else {
            showScreen("screen-select");
        }
    } catch (err) {
        errorBox.textContent = err.message;
    }
}

async function handleSelectCharacter(name) {
    const errorBox = document.getElementById("select-error");
    errorBox.textContent = "";

    try {
        const charSummary = await selectCharacter(name);
        state.activeCharacter = charSummary;
        enterTown(charSummary);
    } catch (err) {
        errorBox.textContent = err.message;
    }
}

function enterTown(character) {
    document.getElementById("town-title").textContent = `${character.town || 'Densewood'} Square`;
    document.getElementById("player-vitals").innerHTML = `
        <strong>${character.name}</strong> | Level ${character.level} | Class: ${character.characterClass}
    `;
    showScreen("screen-town");
}

async function handleCreateCharacter(e) {
    e.preventDefault();

    const nameInput = document.getElementById("new-character-name");
    const classSelect = document.getElementById("new-character-class");
    const errorBox = document.getElementById("select-error");

    const name = nameInput.value.trim();
    const characterClass = classSelect.value;

    errorBox.textContent = "";

    try {
        await createCharacter(name, characterClass);

        nameInput.value = "";

        await handleSelectCharacter(name);
    } catch (err) {
        errorBox.textContent = err.message;
    }
}

// --- Combat UI & Flow ---

function appendCombatLog(messages) {
    const logBox = document.getElementById("combat-log");
    if (!messages) return;

    const list = Array.isArray(messages) ? messages : [messages];
    list.forEach(msg => {
        const line = document.createElement("div");
        line.textContent = `> ${msg}`;
        logBox.appendChild(line);
    });
    logBox.scrollTop = logBox.scrollHeight;
}

function updateCombatVitals(combatState) {
    const player = combatState.player;
    const enemy = combatState.enemy;

    if (player) {
        document.getElementById("combat-player-name").textContent = player.name || "Player";
        document.getElementById("combat-player-hp").textContent =
            `HP: ${player.currentHealth} / ${player.maxHealth}`;
    }
    if (enemy) {
        document.getElementById("combat-enemy-name").textContent = enemy.name || "Enemy";
        document.getElementById("combat-enemy-hp").textContent =
            `HP: ${enemy.currentHealth} / ${enemy.maxHealth}`;
    }
}

function startCombatView(combatState) {
    showScreen("screen-combat");
    document.getElementById("combat-error").textContent = "";
    document.getElementById("combat-log").innerHTML = "";
    document.getElementById("combat-action-buttons").style.display = "block";
    document.getElementById("combat-post-buttons").style.display = "none";
    document.getElementById("btn-combat-attack").disabled = false;

    updateCombatVitals(combatState);
    const enemyName = combatState.enemy ? combatState.enemy.name : "Enemy";
    appendCombatLog(`Encounter started with ${enemyName}!`);
}

async function handleCombatAttack() {
    const errorBox = document.getElementById("combat-error");
    errorBox.textContent = "";

    try {
        const result = await sendCombatAction("ATTACK", null);

        const combatState = result.combatState || result;
        updateCombatVitals(combatState);

        if (combatState.combatLog && combatState.combatLog.length > 0) {
            appendCombatLog(combatState.combatLog);
        }

        if (combatState.terminal) {
            document.getElementById("btn-combat-attack").disabled = true;
            document.getElementById("combat-action-buttons").style.display = "none";
            document.getElementById("combat-post-buttons").style.display = "block";

            if (combatState.state === "VICTORY") {
                appendCombatLog("Victory! The enemy has been defeated.");
            } else if (combatState.state === "DEFEAT") {
                appendCombatLog("Defeat! You have fallen in battle.");
                document.getElementById("btn-combat-continue").textContent = "Return to Town";
            }
        }
    } catch (err) {
        errorBox.textContent = err.message;
    }
}


// --- App Initialization ---

window.addEventListener("DOMContentLoaded", () => {
    loadCharacterSelectScreen();

    // Character creation form
    const createForm = document.getElementById("create-character-form");
    if (createForm) {
        createForm.addEventListener("submit", handleCreateCharacter);
    }

    // Switching character (back to select)
    document.getElementById("btn-logout").addEventListener("click", () => {
        state.activeCharacter = null;
        showScreen("screen-select");
        loadCharacterSelectScreen();
    });

    // Town -> Dungeon Gate
    document.getElementById("btn-to-dungeon-gate").addEventListener("click", () => {
        loadDungeonGateScreen();
    });

    // Dungeon Gate -> Town
    document.getElementById("btn-back-to-town").addEventListener("click", () => {
        if (state.activeCharacter) {
            enterTown(state.activeCharacter);
        } else {
            showScreen("screen-select");
        }
    });

    // Dungeon events
    document.getElementById("btn-explore-room").addEventListener("click", handleExplore);
    document.getElementById("btn-leave-dungeon").addEventListener("click", handleLeave);

    // Combat events
    document.getElementById("btn-combat-attack").addEventListener("click", handleCombatAttack);

    // Continue after battle
    document.getElementById("btn-combat-continue").addEventListener("click", () => {
        const continueBtn = document.getElementById("btn-combat-continue");
        if (continueBtn.textContent === "Return to Town") {
            if (state.activeCharacter) {
                enterTown(state.activeCharacter);
            } else {
                showScreen("screen-select");
            }
        } else {
            document.getElementById("dungeon-event-text").textContent = "Enemy killed. Choose your next move.";
            showScreen("screen-dungeon-run");
        }
    });
});