package se.dungeonsandmorons.web.dto;

import se.dungeonsandmorons.combat.CombatEngine;

import java.util.List;

public record CombatStateResponse(
        String state,
        int roundCount,
        CombatantStatusDto player,
        CombatantStatusDto enemy,
        List<String> availableSkills,
        List<String> combatLog,
        boolean terminal
) {
    public static CombatStateResponse from(CombatEngine engine) {
        var p = engine.getPlayer();
        var e = engine.getEnemy();

        var playerDto = new CombatantStatusDto(
                p.getName(), p.getLevel(), p.getCurrentHealth(), p.getMaxHealth(),
                p.getCurrentMana(), p.getMaxMana(), p.getArmor()
        );

        var enemyDto = new CombatantStatusDto(
                e.getName(), e.getLevel(), e.getCurrentHealth(), e.getMaxHealth(),
                0, 0, e.getArmor()
        );

        return new CombatStateResponse(
                engine.getState().name(),
                engine.getRoundCount(),
                playerDto,
                enemyDto,
                p.getSkillNames(),
                engine.getCombatLog().stream().map(Object::toString).toList(),
                engine.getState().isTerminal()
        );
    }
}