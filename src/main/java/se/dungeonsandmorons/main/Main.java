package se.dungeonsandmorons.main;

public class Main {


    private final Game game = new Game();


    void main(String[] args) {

        game.createDungeon();
        game.mainMenu();


    }

}
