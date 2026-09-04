package se.dungeonsandmorons.main;

import se.dungeonsandmorons.SaveRepository;
import se.dungeonsandmorons.model.combat.CombatService;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.npc.Bird;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.npc.Npc;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final CombatService combatService = new CombatService();
    private Character player;

    private void mainMenu() {

        IO.println("Welcome to Dungeons and Morons!");
        //while (true) {
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
                }
                case "load"     -> {
                    loadCharacterMenu();
                }
                case "quit"     -> {
                    IO.println("Exiting game..");
                }
            }


        //}

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
            case "warrior" -> player = new Warrior(characterName);
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
        player = SaveRepository.load(characterName);
        IO.println("Successfully loaded player " + player.getName());
    }

    public void listSavedCharacters() {

    }

    public void runGame() {

        List<Npc> monsters = new ArrayList<>();
        monsters.add(new Dragon("Green Dragon", 11, 50));
        monsters.add(new Bird("Parrot", 199, 5));

        mainMenu();

        while (true) {

            String input = IO.readln("> ");
            switch (input) {
                case String s when s.toLowerCase().startsWith("attack ") -> {
                    String targetName = s.substring("attack ".length()).trim();
                    for (Npc monster : monsters) {
                        if (monster.getName().equalsIgnoreCase(targetName)) {

                            combatService.engageCombat(monster, player);
                        }
                    }
                }
                case "stats" -> player.getStats();
                case "monsters" -> {
                    IO.println("Monsters in area:");
                    for (Npc monster : monsters) {
                        IO.println("- " + monster.getName());
                    }
                }
                case String s when s.toLowerCase().startsWith("load ") -> {
                    String charName = s.substring("load ".length()).trim();
                    player = SaveRepository.load(charName);
                    IO.println(player);
                }
                case "save" -> SaveRepository.save(player);
                case "help" -> {
                    IO.print("""
                            Available commands:
                            attack monsterName - Engages in combat with specified monster
                            stats              - Shows player stats
                            monsters           - Shows monsters in area
                            load playerName    - Loads specified character
                            save               - Saves current character progress
                            characters         - Lists all saved characters
                            help               - Lists all available commands
                            """);
                }
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
