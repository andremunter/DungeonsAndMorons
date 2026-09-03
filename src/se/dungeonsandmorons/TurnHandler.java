package se.dungeonsandmorons;

import se.dungeonsandmorons.model.combat.Attacker;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.model.character.Character;

public class TurnHandler {

    public static void handleEnemyTurn(Npc enemy, Character player) {
        if (enemy instanceof Attacker attacker) {
            attacker.attack(player, enemy.getSkills().getFirst());
        } else {
            IO.println(enemy.getName() + " is peaceful and does not attack back.");
        }
    }

}
