package se.dungeonsandmorons.main;

import se.dungeonsandmorons.model.combat.CombatService;
import se.dungeonsandmorons.userinterface.UserInterface;

public class Main {


    private static final Game game = new Game();
    private final UserInterface userInterface = new UserInterface();
    private final CombatService combatService = new CombatService();
    private final Initialization init = new Initialization();


    void main(String[] args) {

        //init.createDungeons();

        game.startGame(userInterface, combatService);


    }

}
