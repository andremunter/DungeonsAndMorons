package se.dungeonsandmorons;

import se.dungeonsandmorons.game.Game;
import se.dungeonsandmorons.game.GameStateMachine;
import se.dungeonsandmorons.model.combat.CombatService;
import se.dungeonsandmorons.userinterface.UserInterface;

public class Main {


    private final UserInterface userInterface = new UserInterface();
    private final GameStateMachine stateMachine = new GameStateMachine();
    private final CombatService combatService = new CombatService();
    private final Game game = new Game(userInterface, stateMachine, combatService);


    void main(String[] args) {

        game.startGame();


    }

}
