package se.dungeonsandmorons.web.dto;

import java.util.List;

public record CombatStateResponse(
        String state,
        int roundCount,
        CombatantStatusDto player,
        CombatantStatusDto enemy,
        List<String> availableSkills,
        List<String> combatLog,
        boolean terminal
) {}