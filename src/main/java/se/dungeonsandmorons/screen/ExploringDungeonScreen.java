package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.combat.CombatEngine;

public class ExploringDungeonScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println(String.format("- Exploring %s, Area level %d -", context.getActiveDungeon().getName(), context.getActiveDungeon().getAreaLevel()));
        IO.println("Type 'help' for available commands.");
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {

            case "explore"                      -> {
                // TODO: 'DUNGEON_EVENT' > Calc if ENCOUNTER_MONSTER, ENCOUNTER_DUNGEON_EVENT? _CHEST etc?
                CombatEngine combat = new CombatEngine(context.getPlayer(), context.getActiveDungeon().getMonsters().getFirst());
                combat.startRound();
                context.setActiveCombat(combat);
                context.getStateMachine().transition(GameEvent.ENCOUNTER_MONSTER);
            }
            case "back", "town", "leave"        -> {
                IO.println("Leaving dungeon..");
                context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            }
            case "help"                         -> displayHelp();
        }
    }

    public void displayHelp() {
        IO.print("""
            Available commands:
            explore             - Explores dungeon, encounters monsters and other events.
            back, leave, town   - Leaves dungeon & enters town
            """);
    }
}
