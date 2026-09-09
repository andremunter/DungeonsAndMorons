package se.dungeonsandmorons.game;

public class GameStateMachine {

    public GameState transition(GameState currentState, GameEvent event) {

        return switch (currentState) {

            case IN_TOWN -> switch (event) {
                case ENTER_DUNGEON      -> GameState.EXPLORING_DUNGEON;
                case REST               -> GameState.IN_TOWN;
                default                 -> throw invalidTransition(currentState, event);
            };

            case EXPLORING_DUNGEON -> switch (event) {
                case ENCOUNTER_MONSTER  -> GameState.COMBAT_PLAYER_TURN;
                case LEAVE_DUNGEON      -> GameState.IN_TOWN;
                default                 -> throw invalidTransition(currentState, event);
            };

            case COMBAT_PLAYER_TURN -> switch (event) {
                case ATTACK, CAST       -> GameState.COMBAT_ENEMY_TURN;
                case FLEE               -> GameState.EXPLORING_DUNGEON;
                default                 -> throw invalidTransition(currentState, event);
            };

            case COMBAT_ENEMY_TURN -> switch (event) {
                case ENEMY_ATTACKED     -> GameState.COMBAT_PLAYER_TURN;
                case PLAYER_DIED        -> GameState.GAME_OVER;
                case ENEMY_DIED         -> GameState.VICTORY;
                default                 -> throw invalidTransition(currentState, event);
            };

            case VICTORY -> switch (event) {
                case LOOT_COLLECTED     -> GameState.EXPLORING_DUNGEON;
                default                 -> throw invalidTransition(currentState, event);
            };

            case GAME_OVER -> throw new IllegalStateException("Game Over.");
        };

    }

    private IllegalStateException invalidTransition(GameState state, GameEvent event) {
        return new IllegalStateException("Cannot trigger event " + event + " while in state " + state);
    }

}
