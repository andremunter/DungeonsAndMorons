package se.dungeonsandmorons.model.combat;

import se.dungeonsandmorons.TurnHandler;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.npc.Npc;

public class CombatService {

    public void engageCombat(Npc enemy, Character player) {
        IO.println(String.format("%s HP: %d | %s HP: %d", player.getName(), player.getCurrentHealth(), enemy.getName(), enemy.getCurrentHealth()));
        IO.println("Actions: attack | heal | run");
        String action = IO.readln("> ");
        while (enemy.isAlive() && player.isAlive()) {
            switch (action) {
                case "attack" -> {
                    player.attack(enemy, player.getSkills().getFirst());

                    TurnHandler.handleEnemyTurn(enemy, player);
                }
            }


        }

    }

}
