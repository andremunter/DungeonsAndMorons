package se.dungeonsandmorons.model.combat;

import se.dungeonsandmorons.TurnHandler;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.model.skill.Skill;

import static se.dungeonsandmorons.game.GameData.player;

public class CombatService {

    private final TurnHandler turnHandler = new TurnHandler();

    public boolean engageCombat(Npc enemy, Character player) {
        while (enemy.isAlive() && player.isAlive()) {
            IO.println(String.format("- TURN %d -", turnHandler.getTurnCount()));

            IO.println(String.format("%s HP: %d | %s HP: %d", player.getName(), player.getCurrentHealth(), enemy.getName(), enemy.getCurrentHealth()));
            IO.println("Actions: attack | run");
            String action = IO.readln("> ");

            switch (action) {
                case String s when s.toLowerCase().startsWith("cast ") -> {
                    String spellName = s.substring("cast ".length()).trim();
                    Skill skill = findPlayerSkillByName(spellName);
                    if (skill != null) {
                        turnHandler.handleTurn(enemy, player, PlayerCombatAction.CAST, skill);
                    } else {
                        IO.println("Error: Skill is Null!");
                    }
                }
                case "attack"   -> turnHandler.handleTurn(enemy, player, PlayerCombatAction.ATTACK, null);
                case "run"      -> turnHandler.handleTurn(enemy, player, PlayerCombatAction.RUN, null);
                default         -> IO.println("Wrong input.");
            }

        }
        if (player.isAlive() && !enemy.isAlive()) {
            return true;
        }
        return false;
    }

    public Skill findPlayerSkillByName(String name) {
        if (!player.getSkills().isEmpty()) {
            for (Skill skill : player.getSkills()) {
                if (skill.getName().equalsIgnoreCase(name)) {
                    return skill;
                }
            }
            IO.println("You have not learned this skill.");
        }
        return null;
    }




    public void escapeFight() {
        IO.println("NOT IMPLEMENTED! GL :)");
    }

}
