package se.dungeonsandmorons.model.combat;

public enum CombatState {
    ROUND_START,
    AWAITING_PLAYER_INPUT,
    RESOLVING_PLAYER_ACTION,
    RESOLVING_ENEMY_ACTION,

    VICTORY,
    DEFEAT,
    ESCAPED;

    public boolean isTerminal() {
        return this == VICTORY || this == DEFEAT || this == ESCAPED;
    }
}
