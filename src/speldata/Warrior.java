package speldata;

public class Warrior extends Character implements Targetable, Attacker{
    int damage = 3;

    public Warrior(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }

    @Override
    public void attack(Targetable targetable) {
        if (targetable instanceof Attacker){
            IO.println("Target can fight back");
        }
        int damageDealt = calculateDamage(targetable);
        IO.println("Warrior swung their axe and dealt " + damageDealt + " damage to " + targetable.getClass().getSimpleName());
        targetable.takeDamage(damageDealt);
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp() - damage);
    }

    private int calculateDamage(Targetable targetable){
        int damage = getStrength() * 2;

        if (targetable instanceof Flyable){
            IO.println("Målet flyger, du halvmissade yxkastet");
            damage /= 2;
        }

        return damage;
    }
}
