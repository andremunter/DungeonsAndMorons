package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;


public class MainMenuScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.print("""
                
                === Dungeons and Morons ===
                
                """);
        IO.print("""
                1. New Game
                2. Load
                3. Quit
                
                """);
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {
            case "1", "new game" -> context.getStateMachine().transition(GameEvent.START_CREATE_CHARACTER);
            case "2", "load" -> context.getStateMachine().transition(GameEvent.OPEN_LOAD_MENU);
            case "3", "quit" -> System.exit(0);
        }
    }

}
