package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.character.Character;

import java.util.List;

public class LoadScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.print(String.format("""
                
                === Saved Characters ===
                
                """));
        List<Character> characters = context.getSaveRepository().fetchSaves();

        if (characters != null && !characters.isEmpty()) {
            for (Character character : characters) {
                IO.println(String.format("- %s, %s, level: %d", character.getName(), character.getCharacterClass(), character.getLevel()));
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

        boolean success = context.getSaveRepository().load(context, trimmed);
        if (success) {
            IO.println(String.format("Successfully loaded %s.", trimmed));
            context.getStateMachine().transition(GameEvent.LOAD_GAME);
        } else {
            IO.println(String.format("Could not find save named '%s'", trimmed));
            context.getStateMachine().transition(GameEvent.LOAD_GAME_FAIL);
        }

    }

}
