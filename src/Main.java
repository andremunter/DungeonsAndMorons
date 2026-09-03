import speldata.*;
import speldata.Character;


void main() {
    Attacker[] attackers = {
            new Warrior(5, 2, 1),
            new Archer(2, 4, 1),
            new Magician(1, 2, 5)
    };

    Dragon dragon = new Dragon();

    IO.println("Dragons hp = " + dragon.getHp());

    for (Attacker attacker : attackers){
        attacker.attack(dragon);
        IO.println("Dragons hp = " + dragon.getHp());
    }

    dragon.fly();
}