package speldata;

public class Bird extends Npc implements Targetable, Flyable{
    @Override
    public void fly() {

    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp() - damage);
        IO.println("Bird took " + damage + " damage, current hp = " + getHp());
    }
}
