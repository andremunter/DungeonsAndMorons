package se.dungeonsandmorons.web.dto;

public record CombatantStatusDto(
        String name,
        int level,
        int currentHealth,
        int maxHealth,
        int currentMana,
        int maxMana,
        int armor
) {}