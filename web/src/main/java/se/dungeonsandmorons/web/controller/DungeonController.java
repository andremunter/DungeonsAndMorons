package se.dungeonsandmorons.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.web.dto.DungeonResponse;
import se.dungeonsandmorons.web.dto.MonsterSummaryResponse;

import java.util.List;

@Tag(name = "Dungeons", description = "Query available dungeons and encounters information")
@RestController
@RequestMapping("/api/dungeons")
public class DungeonController {

    private final DungeonService dungeonService;

    public DungeonController(DungeonService dungeonService) {
        this.dungeonService = dungeonService;
    }

    @Operation(summary = "List all discoverable dungeons with encounter summaries")
    @GetMapping
    public List<DungeonResponse> getAllDungeons() {
        return dungeonService.getAvailableDungeonNames().stream()
                .map(dungeonService::findDungeonByName)
                .map(this::toResponse)
                .toList();
    }

    @Operation(summary = "Get dungeon details and monster spawns by name")
    @GetMapping("/{name}")
    public DungeonResponse getDungeonByName(@PathVariable String name) {
        String formattedName = name.replace("-", " ").trim();
        Dungeon dungeon = dungeonService.findDungeonByName(formattedName);
        return toResponse(dungeon);
    }

    private DungeonResponse toResponse(Dungeon dungeon) {
        List<MonsterSummaryResponse> monsters = dungeon.getMonsters().stream()
                .map(m -> new MonsterSummaryResponse(
                        m.getName(),
                        m.getMaxHealth(),
                        m.getExpOnDeath(),
                        m.getRarity().name()
                ))
                .toList();

        return new DungeonResponse(
                dungeon.getName(),
                dungeon.getDescription(),
                dungeon.getAreaLevel(),
                monsters.size(),
                monsters
        );
    }

}
