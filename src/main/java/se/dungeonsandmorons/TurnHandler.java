package se.dungeonsandmorons;

import se.dungeonsandmorons.main.Game;
import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.combat.CombatState;
import se.dungeonsandmorons.model.combat.PlayerCombatAction;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.skill.Skill;

public class TurnHandler {

    private int turnCount = 1;

    public void handleTurn(Npc enemy, Character player, PlayerCombatAction playerAction, Skill skill) {

        switch (playerAction) {
            case PlayerCombatAction.ATTACK  -> {
                player.attack(enemy, player.getSkills().getFirst());
                handleEnemyTurn(enemy, player);
            }
            case PlayerCombatAction.CAST    -> {
                player.attack(enemy, skill);
                handleEnemyTurn(enemy, player);
            }
            case PlayerCombatAction.RUN     -> Game.combatService.escapeFight();
        }

        turnCount++;

        if (!player.isAlive()) {
            IO.println(String.format("%s(%d) died fighting %s.", player.getName(), player.getLevel(), enemy.getName()));
        }
    }

    public static void handleEnemyTurn(Npc enemy, Character player) {
        if (enemy instanceof Attacker attacker) {
            attacker.attack(player, enemy.getSkills().getFirst());
        } else {
            if (enemy.isAlive()) {
                IO.println(enemy.getName() + " is peaceful and does not attack back.");
            }
        }
    }

    public static void playerAction(PlayerCombatAction action) {

    }

    public int getTurnCount() { return turnCount; }

}
