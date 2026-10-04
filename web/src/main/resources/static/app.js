
// --- Global Game State ---
const state = {
    activeCharacter: null
};

// --- Screen Switching ---
function showScreen(screenId) {
    document.getElementById("screen-select").style.display = "none";
    document.getElementById("screen-town").style.display = "none";
    document.getElementById("screen-dungeon-gate").style.display = "none";

    const activeScreen = document.getElementById(screenId);
    if (activeScreen) {
        activeScreen.style.display = "block";
    }
}




// --- API Functions ---

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
            listContainer.innerHTML = "<p>No dungeons available right now<p/>";
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
        const message = await enterDungeon(dungeonName);
        console.log(message);
        alert(message);
        /// Next step :: transition to active dungeon exploration screen
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


// --- App Initialization ---

window.addEventListener("DOMContentLoaded", () => {
    loadCharacterSelectScreen();

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
});