package se.dungeonsandmorons.game;

import se.dungeonsandmorons.screen.*;

import java.util.Map;

public class GameLoop {

    private final GameContext context;
    private final Map<GameState, Screen> screens;

    public GameLoop(GameContext context) {
        this.context = context;
        this.screens = Map.of(
                GameState.MAIN_MENU, new MainMenuScreen(),
                GameState.LOAD_MENU, new LoadScreen(),
                GameState.CHARACTER_CREATION, new CreateCharacterScreen(),
                GameState.IN_TOWN, new TownScreen(),
                GameState.TALKING_TO_NPC, new TalkingToNpcScreen(),
                GameState.SELECTING_DUNGEON, new DungeonSelectScreen(),
                GameState.EXPLORING_DUNGEON, new ExploringDungeonScreen(),
                GameState.IN_COMBAT, new CombatScreen()
        );
    }

    public void run() {
        while (context.getStateMachine().getCurrentState() != GameState.GAME_OVER) {
            GameState currentState = context.getStateMachine().getCurrentState();
            Screen currentScreen = screens.get(currentState);

            if (currentScreen == null) {
                throw new IllegalStateException("No screen mapped for state: NULL");
            }

            currentScreen.display(context);
            String input = IO.readln("> ");
            currentScreen.handleInput(input, context);
        }
    }

}
