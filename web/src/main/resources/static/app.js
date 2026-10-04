
// --- Global Game State ---
const state = {
    activeCharacter: null
};

// --- Screen Switching ---
function showScreen(screenId) {
    document.getElementById("screen-select").style.display = "none";
    document.getElementById("screen-town").style.display = "none";

    const activeScreen = document.getElementById(screenId);
    if (activeScreen) {
        activeScreen.style.display = "block";
    }
}




// --- API Functions ---

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
});