package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;

public class CombatScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println("- Combat -");
        IO.println("Type 'help' for available commands.");
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {

        }
    }
}
