package se.dungeonsandmorons.model.character;

public interface Combatant {
    String getName();
    int getCurrentHealth();
    int getMaxHealth();
    boolean takeDamage(int amount, Combatant combatant);
    default boolean isAlive() {
        return getCurrentHealth() > 0;
    }
    default boolean isDead() {
        return getCurrentHealth() <= 0;
    }
}
