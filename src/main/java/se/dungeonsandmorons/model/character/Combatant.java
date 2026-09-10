package se.dungeonsandmorons.model.character;

public interface Combatant {
    String getName();
    int getCurrentHealth();
    int getMaxHealth();
    void takeDamage(int amount);
    default boolean isAlive() {
        return getCurrentHealth() > 0;
    }
}
