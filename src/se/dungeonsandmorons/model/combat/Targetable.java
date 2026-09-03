package se.dungeonsandmorons.model.combat;

public interface Targetable {
    void takeDamage(int damage);
    String getName();
    int getCurrentHealth();
    boolean isAlive();

}
