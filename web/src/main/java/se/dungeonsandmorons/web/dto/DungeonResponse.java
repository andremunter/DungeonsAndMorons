package se.dungeonsandmorons.web.dto;

import java.util.List;

public record DungeonResponse(
        String name,
        String description,
        int areaLevel,
        int monsterCount,
        List<MonsterSummaryResponse> monsters
) {}
