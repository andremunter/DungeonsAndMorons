package se.dungeonsandmorons.model.combat;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.model.skill.SkillRegistry;


public class CombatService {


    public boolean runCombatLoop(Npc enemy, Character player) {
        CombatEngine engine = new CombatEngine(player, enemy);
        engine.startRound();

        while (!engine.getState().isTerminal()) {
            IO.println(String.format("""
                    ---------- ROUND %d ----------
                    %s HP: %d | %s HP: %d
                    """,
                    engine.getRoundCount(),
                    player.getName(), player.getCurrentHealth(),
                    enemy.getName(), enemy.getCurrentHealth()));
            IO.println("Actions: Attack | Cast <skill> | Run");

            String input = IO.readln("> ");
            handleUserInput(engine, player, input);
        }

        return switch (engine.getState()) {
            case VICTORY -> {
                IO.println(enemy.getName() + " was defeated.");
                yield true;
            }
            case DEFEAT -> {
                IO.println(player.getName() + " was killed.");
                yield false;
            }
            case ESCAPED -> {
                IO.println("Fled from combat.");
                yield false;
            }
            default -> false;
        };
    }

    public void handleUserInput(CombatEngine engine, Character player, String input) {

        String trimmed = input.trim();
        String[] parts = trimmed.split("\\s+", 2);
        String action = parts[0].toLowerCase();
        String skill = parts.length > 1 ? parts[1].trim() : "";

        switch (action) {
            case "cast" -> {
                if (skill.isEmpty()) {
                    IO.println("Cannot cast nothing. Usage: Cast <skill>");
                    break;
                }

                if (player.isSkillLearned(SkillRegistry.get(skill))) {
                    engine.executePlayerAction(CombatAction.CAST, SkillRegistry.get(skill));
                } else {
                    IO.println("Skill not learned.");
                }
            }
            case "attack" -> engine.executePlayerAction(CombatAction.ATTACK, player.getSkills().getFirst());
            case "run" -> engine.executePlayerAction(CombatAction.RUN, null);
            default -> IO.println("Action not found.");
        }
    }

}
