package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;

public class ExploringDungeonScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println("- Exploring <DungeonName> -");
        IO.println("Type 'help' for available commands.");
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {

        }
    }
}
