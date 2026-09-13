package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.town.TownRegistry;

import java.util.List;

public class MainMenuScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println("Welcome to Dungeons and Morons!");
        IO.print("""
                    Choose by typing:
                    New Game
                    Load
                    Quit
                    """);
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {
            case "new game"     -> {
                createCharacter(context);
                context.setActiveTown(TownRegistry.get("Densewood")); // FIRST Town a new player encounters
                context.getStateMachine().transition(GameEvent.CREATE_CHARACTER);
            }
            case "load"         -> {
                if (loadCharacter(context)) {
                    IO.println("Successfully loaded " + context.getPlayer().getName());
                    context.getStateMachine().transition(GameEvent.LOAD_GAME);
                } else {
                    IO.println("Failed to load character.");
                    context.getStateMachine().transition(GameEvent.LOAD_GAME_FAIL);
                }
            }
            case "quit"         -> System.exit(0);
        }
    }

    private void createCharacter(GameContext context) {
        IO.println("- Character Creation -");
        IO.println("Enter your name:");

        String characterName = IO.readln().trim();

        IO.print("""
                Choose class by typing one of the following:
                Warrior
                Magician
                Archer
                """);
        String characterClass = IO.readln("> ");

        switch (characterClass.trim().toLowerCase()) {
            case "warrior" -> context.setPlayer(new Warrior(characterName));
            //case "magician" -> context.setPlayer(new Warrior(characterName));
            //case "archer" -> context.setPlayer(new Warrior(characterName));
            default -> {
                IO.println("Class does not exist.");
            }
        }
    }

    private boolean loadCharacter(GameContext context) {

        listSaves(context);

        IO.println("Load character by typing its name(case-sensitive)");
        String charName = IO.readln("> ");

        Character savedPlayer = context.getSaveRepository().load(charName);
        if (savedPlayer != null) {
            context.setPlayer(savedPlayer);
            return true;
        }
        return false;
    }

    private static void listSaves(GameContext context) {
        List<Character> characters = context.getSaveRepository().fetchSaves();
        if (characters != null && !characters.isEmpty()) {
            IO.println("- Saved Characters -");
            for (Character character : characters) {
                IO.println(String.format("%s, %s, level: %d", character.getName(), character.getClass().getSimpleName(), character.getLevel()));
            }
        } else {
            IO.println("No saved characters");
        }
    }

}
