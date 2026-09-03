package se.dungeonsandmorons.main;

import se.dungeonsandmorons.TurnHandler;
import se.dungeonsandmorons.model.npc.Bird;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.npc.Npc;

public class Main {

    static void main(String[] args) {

        Character warrior = new Warrior("Gucki");

        Npc dragon = new Dragon("Green Dragon", 199, 50);
        Npc bird = new Bird("Parrot", 199, 5);


        warrior.attack(dragon, warrior.getSkills().getFirst());
        warrior.attack(dragon, warrior.getSkills().getFirst());


        TurnHandler.handleEnemyTurn(dragon, warrior);
        TurnHandler.handleEnemyTurn(bird, warrior);
    }

}
