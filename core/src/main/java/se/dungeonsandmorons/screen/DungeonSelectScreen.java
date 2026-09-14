package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.helper.CapitalizeWords;
import se.dungeonsandmorons.model.dungeon.Dungeon;

import java.util.List;

public class DungeonSelectScreen implements Screen {

    @Override
    public void display(GameContext context) {
        List<String> dungeons = context.getDungeonService().getAvailableDungeonNames();
        if (dungeons == null || dungeons.isEmpty()) {
            IO.println("No dungeons discovered.");
        }

        IO.println("- Dungeons -");

        for (String dungeon : dungeons) {
            IO.println(context.getDungeonService().findDungeonByName(dungeon));
        }

        IO.println("Type 'help' for available commands.");
    }

    @Override
    public void handleInput(String input, GameContext context) {
        String fInput = input.trim().toLowerCase();
        if (context.getDungeonService().getAvailableDungeonNames().contains(fInput)) {
            context.setActiveDungeon(context.getDungeonService().findDungeonByName(fInput));

            IO.println(String.format("Entering %s... Proceed?(enter), 'back' to cancel.", context.getActiveDungeon().getName()));
            String enter = IO.readln(">");

            switch (enter.trim().toLowerCase()) {
                case "enter", "", " " -> {
                    context.getStateMachine().transition(GameEvent.ENTER_DUNGEON);
                }
                case "back", "cancel" -> context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            }
        }




        switch (input.trim().toLowerCase()) {
            case "leave"    -> context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            case "help"     -> displayHelp();
        }
    }

    public void displayHelp() {
        IO.print("""
            Available commands:
            <DungeonName>   - Enters specified dungeon.
            Leave           - Back to town
            Help            - Help :)
            """);
    }
}
