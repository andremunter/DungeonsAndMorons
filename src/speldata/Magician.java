package speldata;

public class Magician extends Character implements Targetable, Attacker{

    public Magician(int strength, int agility, int intelligence){
        super(strength, agility, intelligence);
    }

    @Override
    public void attack(Targetable targetable) {
        targetable.takeDamage(calculateDamage());
        IO.println("Magician threw their wand and dealt " + calculateDamage() + " damage");
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp()-damage);
    }

    private int calculateDamage(){
        return getIntelligence() * 2;
    }

}
