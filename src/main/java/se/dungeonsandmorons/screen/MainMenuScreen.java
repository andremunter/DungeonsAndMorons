package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;

public class MainMenuScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println("Welcome to Dungeons and Morons!");
        IO.print("""
                    Choose by typing:
                    New Game
                    Load
                    Quit
                    """);
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {
            case "new game"     -> context.getStateMachine().transition(GameEvent.CREATE_CHARACTER);
            case "load"         -> context.getStateMachine().transition(GameEvent.LOAD_GAME);
            case "quit"         -> System.exit(0);
        }
    }

}
