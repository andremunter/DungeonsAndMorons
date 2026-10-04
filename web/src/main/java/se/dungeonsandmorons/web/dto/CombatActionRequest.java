package se.dungeonsandmorons.web.dto;

import se.dungeonsandmorons.combat.CombatAction;

public record CombatActionRequest(
        CombatAction action,
        String skillName
) {}