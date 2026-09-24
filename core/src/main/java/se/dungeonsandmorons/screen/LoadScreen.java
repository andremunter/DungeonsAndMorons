package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.exception.CorruptedSaveException;
import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;

import java.util.List;

public class LoadScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.print(String.format("""
                
                === Saved Characters ===
                
                """));
        List<CharacterSummary> saves = context.getSaveService().getAvailableSaves();

        if (saves != null && !saves.isEmpty()) {
            for (CharacterSummary save : saves) {
                IO.println(save);
            }
        } else {
            IO.println("No saved characters");
        }

        IO.print("""
                
                Type character name to load, or 'back' to return.
                Name is case-sensitive
                
                """);
    }

    @Override
    public void handleInput(String input, GameContext context) {
        String trimmed = input.trim();

        if (trimmed.equalsIgnoreCase("back")) {
            context.getStateMachine().transition(GameEvent.CANCEL);
            return;
        }

        try {
            boolean success = context.getSaveRepository().load(context, trimmed);
            if (success) {
                IO.println(String.format("Successfully loaded %s.", trimmed));
                context.getStateMachine().transition(GameEvent.LOAD_GAME);
            } else {
                IO.println(String.format("Could not find save named '%s'", trimmed));
                context.getStateMachine().transition(GameEvent.LOAD_GAME_FAIL);
            }
        } catch (CorruptedSaveException e) {
            IO.println("Error: " + e.getMessage());
            IO.println("Returning to Main Menu...");
            context.getStateMachine().transition(GameEvent.LOAD_GAME_FAIL);
        }

    }

}
