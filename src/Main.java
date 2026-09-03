import speldata.*;
import speldata.Character;


void main() {
    Attacker[] attackers = {
            new Warrior(5, 2, 1),
            new Archer(2, 4, 1),
            new Magician(1, 2, 5)
    };

    Targetable[] targets = {
            new Dragon(),
            new Bird()
    };


    for (Attacker attacker : attackers){
        attacker.attack(targets[0]);
        attacker.attack(targets[1]);
    }

}