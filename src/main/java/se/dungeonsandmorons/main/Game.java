package se.dungeonsandmorons.main;

import se.dungeonsandmorons.SaveRepository;
import se.dungeonsandmorons.model.combat.CombatService;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.npc.Bird;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.userinterface.UserInterface;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameData gameData = new GameData();
    private final CombatService combatService = new CombatService();

    void mainMenu() {

        IO.println("Welcome to Dungeons and Morons!");
        while (true) {
            IO.print("""
                    Choose by typing:
                    New Game
                    Load
                    Quit
                    """);
            String choice = IO.readln("> ").toLowerCase();
            switch (choice) {
                case "new game" -> {
                    createCharacterMenu();
                    runGame();
                }
                case "load"     -> {
                    loadCharacterMenu();
                    runGame();
                }
                case "quit"     -> {
                    IO.println("Exiting game..");
                    return;
                }
            }


        }

    }

    public void createCharacterMenu() {
        IO.println("What are you called?");
        String characterName = IO.readln("> ");
        IO.print("""
                Choose a class by typing one of the following:
                Warrior
                Magician
                Archer
                """);
        String characterClass = IO.readln("> ").toLowerCase();
        switch (characterClass) {
            case "warrior" -> gameData.player = new Warrior(characterName);
            //case "magician" -> player = new Magician(characterName);
            //case "archer" -> player = new Archer(characterName);
            default -> {
                IO.println("Class does not exist.");
            }
        }
    }

    public void loadCharacterMenu() {
        listSavedCharacters();
        IO.println("Choose character by typing its name");
        String characterName = IO.readln("> ");
        gameData.player = SaveRepository.load(characterName);
        IO.println("Successfully loaded player " + gameData.player.getName());
    }

    public void listSavedCharacters() {
        List<Character> characters = SaveRepository.fetchSaves();

        if (!characters.isEmpty()) {
            IO.println("- Saved Characters -");
            for (Character character : characters) {
                IO.println(String.format("%s, %s, level: %d", character.getName(), character.getClass().getSimpleName(), character.getLevel()));
            }
        } else {
            IO.println("No saved characters");
        }
    }

    public void runGame() {

        List<Npc> monsters = new ArrayList<>();
        monsters.add(new Dragon("Green Dragon", 11, 50));
        monsters.add(new Bird("Parrot", 199, 5));



        while (true) {

            String input = IO.readln("> ");
            switch (input) {
                case String s when s.toLowerCase().startsWith("attack ") -> {
                    String targetName = s.substring("attack ".length()).trim();
                    for (Npc monster : monsters) {
                        if (monster.getName().equalsIgnoreCase(targetName)) {

                            combatService.engageCombat(monster, gameData.player);
                        }
                    }
                }
                case "stats" -> gameData.player.getStats();
                case "monsters" -> {
                    IO.println("Monsters in area:");
                    for (Npc monster : monsters) {
                        IO.println("- " + monster.getName());
                    }
                }
                case "save" -> SaveRepository.save(gameData.player);
                case "load" -> loadCharacterMenu();
                case "help" -> UserInterface.helpMenu();
                case "quit", "exit", "terminate" -> {
                    IO.println("Exiting game..");
                    return;
                }
                default -> {
                    IO.println("Wrong input.");
                }
            }
        }
    }

}
