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

                                        // Reminders for myself:
        String trimmed = input.trim();  // trim returns the input string with leading & trailing spaces removed
        String[] parts = trimmed.split("\\s+", 2); // \\s+ removes all spaces between the first and second word
        String action = parts[0].toLowerCase(); // action is part[0], meaning the first word in the input string
        String skill = parts.length > 1 ? parts[1].trim() : ""; // if parts array contains more than 1, we have a second part. trim it

        switch (action) {
            case "cast" -> {
                if (skill.isEmpty()) {
                    IO.println("Cannot cast nothing. Usage: Cast <skill>");
                    break;
                }

                if (player.isSkillLearned(skill)) {
                    engine.executePlayerAction(CombatAction.CAST, SkillRegistry.findSkillByName(skill));
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
