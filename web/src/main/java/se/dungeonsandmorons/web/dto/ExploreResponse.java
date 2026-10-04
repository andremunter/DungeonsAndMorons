package se.dungeonsandmorons.web.dto;

public record ExploreResponse(
        String eventType, // e.g. "COMBAT", "EMPTY", "CLEARED"
        String message,
        CombatStateResponse combatState // null if not a combat encounter
) {}