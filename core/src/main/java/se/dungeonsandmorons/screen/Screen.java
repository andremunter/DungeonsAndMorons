package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;

public interface Screen {
    void display(GameContext context);
    void handleInput(String input, GameContext context);
}
