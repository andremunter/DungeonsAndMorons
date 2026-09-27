package se.dungeonsandmorons.web.dto;

public record MonsterSummaryResponse(
        String name,
        int maxHealth,
        int expOnDeath,
        String rarity
) {
}
