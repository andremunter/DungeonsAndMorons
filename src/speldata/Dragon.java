package speldata;

public class Dragon extends Npc implements Targetable, Attacker, Flyable{

    @Override
    public void attack(Targetable targetable) {

    }

    @Override
    public void fly() {
        IO.println("Dragon flew away");
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp() - damage);
        IO.println("Dragon took " + damage + " damage, current hp = " + getHp());
    }

}
