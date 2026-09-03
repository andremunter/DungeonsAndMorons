package speldata;

public class Archer extends Character implements Targetable, Attacker {
    int damage = 3;

    public Archer(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }
    @Override
    public void attack(Targetable targetable) {
        IO.println("Archer shot their bow and dealt " + calculateDamage(targetable) + " damage");
        targetable.takeDamage(calculateDamage(targetable));
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp()-damage);
    }

    private int calculateDamage(Targetable targetable){
        int damage = getAgility() * 2;

        if (targetable instanceof Flyable){

            damage *= 2;
        }

        return damage;
    }
}
