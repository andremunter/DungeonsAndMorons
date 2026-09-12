package se.dungeonsandmorons.game;

public class GameStateMachine {
    private GameState currentState = GameState.MAIN_MENU;

    public void transition(GameEvent event) {

        this.currentState =  switch (currentState) {

            case MAIN_MENU -> switch (event) {
                case LOAD_GAME, ENTER_TOWN, CREATE_CHARACTER
                                        -> GameState.IN_TOWN;
                default                 -> throw invalidTransition(currentState, event);
            };

            case IN_TOWN -> switch (event) {
                case INTERACT_NPC       -> GameState.TALKING_TO_NPC;
                case SELECT_DUNGEON     -> GameState.SELECTING_DUNGEON;
                case REST               -> GameState.IN_TOWN;
                default                 -> throw invalidTransition(currentState, event);
            };

            case TALKING_TO_NPC -> switch (event) {
                case LEAVE_NPC, ENTER_TOWN  -> GameState.IN_TOWN;
                default                     -> throw invalidTransition(currentState, event);
            };

            case SELECTING_DUNGEON -> switch (event) {
                case ENTER_DUNGEON          -> GameState.EXPLORING_DUNGEON;
                case ENTER_TOWN             -> GameState.IN_TOWN;
                default                     -> throw invalidTransition(currentState, event);
            };

            case EXPLORING_DUNGEON -> switch (event) {
                case ENCOUNTER_MONSTER          -> GameState.IN_COMBAT;
                case LEAVE_DUNGEON, ENTER_TOWN  -> GameState.IN_TOWN;
                default                         -> throw invalidTransition(currentState, event);
            };

            case IN_COMBAT -> switch (event) {
                case COMBAT_RESOLVED    -> GameState.EXPLORING_DUNGEON;
                case FLEE               -> GameState.EXPLORING_DUNGEON;
                case PLAYER_DIED        -> GameState.GAME_OVER;
                default                 -> throw invalidTransition(currentState, event);
            };

            case GAME_OVER -> switch (event) {
                case ENTER_TOWN         -> GameState.IN_TOWN;
                case LOAD_GAME          -> GameState.MAIN_MENU;
                default                 -> throw invalidTransition(currentState, event);
            };
        };
    }

    private IllegalStateException invalidTransition(GameState state, GameEvent event) {
        return new IllegalStateException("Cannot trigger event " + event + " while in state " + state);
    }

    public GameState getCurrentState() {
        return currentState;
    }

}
