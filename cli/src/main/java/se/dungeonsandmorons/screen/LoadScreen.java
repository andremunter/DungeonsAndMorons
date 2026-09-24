package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.exception.CorruptedSaveException;
import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.town.TownRegistry;

import java.util.List;

public class LoadScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.print("""
                
                === Saved Characters ===
                
                """);
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
            context.getSaveService().loadGame(trimmed).ifPresentOrElse(saveData -> {
                context.setPlayer(saveData.player());
                String townName = saveData.lastTownVisited() != null
                        ? saveData.lastTownVisited()
                        : "Densewood";
                context.setActiveTown(TownRegistry.get(townName));

                IO.println("Character loaded successfully!");
                context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            }, () -> IO.println("Failed to load character"));
        } catch (CorruptedSaveException e) {
            IO.println("Error: " + e.getMessage());
            IO.println("Returning to Main Menu...");
            context.getStateMachine().transition(GameEvent.LOAD_GAME_FAIL);
        }

    }

}
