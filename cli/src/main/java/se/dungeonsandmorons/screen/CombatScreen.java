package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.combat.CombatAction;
import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.model.skill.SkillRegistry;

public class CombatScreen implements Screen {

    @Override
    public void display(GameContext context) {

        CombatEngine engine = context.getActiveCombat();
        Kharacter player = context.getPlayer();
        Monster monster = context.getActiveDungeon().getMonsters().getFirst();

        IO.println(String.format("""
                    ---------- ROUND %d ----------
                    %s HP: %d | %s HP: %d
                    """,
                engine.getRoundCount(),
                player.getName(), player.getCurrentHealth(),
                monster.getName(), monster.getCurrentHealth()));

        IO.println("Actions: Attack | Cast <skill> | Run");
    }

    @Override
    public void handleInput(String input, GameContext context) {

        CombatEngine engine = context.getActiveCombat();
        Kharacter player = context.getPlayer();
        Monster monster = context.getActiveDungeon().getMonsters().getFirst();

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
            case "attack"   -> engine.executePlayerAction(CombatAction.ATTACK, player.getSkills().getFirst());
            case "run"      -> {
                // TODO: Calc odds of escaping
                engine.executePlayerAction(CombatAction.RUN, null);
            }
            default -> IO.println("Action not found.");
        }

        switch (engine.getState()) {
            case VICTORY -> {
                IO.println(monster.getName() + " was defeated.");
                context.getActiveDungeon().removeMonster(monster);
                context.setActiveCombat(null);

                if (context.getActiveDungeon().getMonsters().isEmpty()) {
                    IO.println(String.format("%s successfully cleared %s!",
                            context.getPlayer().getName(), context.getActiveDungeon().getName()));

                    context.setActiveDungeon(null);
                    // TODO: GameEvent.DUNGEON_CLEARED ? Grab loot, etc.. ? > ENTER_TOWN/SELECT_DUNGEON
                    context.getStateMachine().transition(GameEvent.LEAVE_DUNGEON);
                } else {
                    context.getStateMachine().transition(GameEvent.COMBAT_RESOLVED);
                }
            }
            case DEFEAT -> {
                IO.println(player.getName() + " was killed.");
                context.setActiveCombat(null);
                context.getStateMachine().transition(GameEvent.PLAYER_DIED);
            }
            case ESCAPED -> {
                IO.println("Fled from combat.");
                context.setActiveCombat(null);
                context.getStateMachine().transition(GameEvent.FLEE);
            }
        }
    }
}
