package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.character.Warrior;

public class CreateCharacterScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println("- Character Creation -");
        IO.println("Enter your name:");
    }

    @Override
    public void handleInput(String input, GameContext context) {

        String characterName = IO.readln().trim();

        IO.print("""
                Choose a class by typing one of the following:
                Warrior
                Magician
                Archer
                """);
        String characterClass = IO.readln("> ");

        switch (input.trim().toLowerCase()) {
            case "warrior" -> {
                context.setPlayer(new Warrior(characterName));
                context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            }
            //case "magician" -> context.setPlayer(new Warrior(characterName));
            //case "archer" -> context.setPlayer(new Warrior(characterName));
            default -> {
                IO.println("Class does not exist.");
            }
        }
    }


}