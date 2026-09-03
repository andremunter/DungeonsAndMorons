package se.dungeonsandmorons.model.combat;

public interface Targetable {
    void takeDamage(int damage, Attacker attacker);
    String getName();
    int getCurrentHealth();
    boolean isAlive();

}
