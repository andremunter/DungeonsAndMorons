import se.dungeonsandmorons.model.*;
import se.dungeonsandmorons.model.Npc.Bird;
import se.dungeonsandmorons.model.Npc.Dragon;
import se.dungeonsandmorons.model.character.Archer;
import se.dungeonsandmorons.model.character.Magician;
import se.dungeonsandmorons.model.character.Warrior;


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