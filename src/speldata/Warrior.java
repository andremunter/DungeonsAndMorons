package speldata;

public class Warrior extends Character implements Targetable, Attacker{
    int damage = 3;

    public Warrior(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }

    @Override
    public void attack(Targetable targetable) {
        targetable.takeDamage(calculateDamage());
        IO.println("Warrior swung their axe and dealt " + calculateDamage() + " damage");
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp() - damage);
    }

    private int calculateDamage(){
        return getStrength() * 2;
    }
}
