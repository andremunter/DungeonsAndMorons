package speldata;

public class Archer extends Character implements Targetable, Attacker {
    int damage = 3;

    public Archer(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }
    @Override
    public void attack(Targetable targetable) {
        targetable.takeDamage(calculateDamage());
        IO.println("Archer shot their bow and dealt " + calculateDamage() + " damage");
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp()-damage);
    }

    private int calculateDamage(){
        return getAgility() * 2;
    }
}
