package se.dungeonsandmorons.userinterface;

import se.dungeonsandmorons.SaveRepository;
import se.dungeonsandmorons.game.Game;
import se.dungeonsandmorons.game.GameData;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.skill.Skill;
import se.dungeonsandmorons.service.DungeonService;

import java.util.List;

public class UserInterface {

    private final DungeonService dungeonService = new DungeonService();
    private Game game;

    public void mainMenu(Game game) {
        this.game = game;

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
                    townMenu();
                }
                case "load"     -> {
                    loadCharacterMenu();
                    townMenu();
                }
                case "quit"     -> {
                    IO.println("Exiting game..");
                    return;
                }
            }


        }

    }

    public void townMenu() {

        while (true) {
            IO.println("- Town -");

            String input = IO.readln("> ");
            switch (input) {
                case "stats"        -> GameData.player.getStats();
                case "skills"       -> listPlayerSkills(GameData.player);
                case "dungeons"     -> enterDungeonMenu();
                case "save"         -> SaveRepository.save(GameData.player);
                case "load"         -> loadCharacterMenu();
                case "help"         -> helpMenu();
                case "main menu"    -> {
                    return;
                }
                default -> {
                    IO.println("Wrong input.");
                }
            }
        }
    }

    public void helpMenu() {
        IO.print("""
            Available commands:
            stats              - Shows player stats
            skills             - Lists learned skills
            dungeons           - Lists available dungeons
            load               - Lists and loads saved characters
            save               - Saves current character progress
            help               - Lists all available commands
            main menu          - Goes back to Main Menu
            """);
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
            case "warrior" -> GameData.player = new Warrior(characterName);
            //case "magician" -> player = new Magician(characterName);
            //case "archer" -> player = new Archer(characterName);
            default -> {
                IO.println("Class does not exist.");
            }
        }
    }

    public void loadCharacterMenu() {
        listSavedCharacters();
        IO.print("""
                Choose character by typing its name
                Type 'back' to go back to main menu
                """);
        String characterName = IO.readln("> ");
        if (characterName.equalsIgnoreCase("back")) {
            mainMenu(game);
        }

        GameData.player = SaveRepository.load(characterName);
        if (GameData.player != null) {
            IO.println("Successfully loaded player " + GameData.player.getName());
        } else {
            IO.println("Character does not exist.");
            return;
        }
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

    public void listDungeons() {
        // TODO: Need to be able to list Area Level as well. Dungeon Names are not sufficient.
        List<String> names = dungeonService.getAvailableDungeonNames();

        if (!names.isEmpty()) {
            IO.println("- Dungeons -");
            for (String dungeonName : names) {
                IO.println(dungeonName);
            }
        } else {
            IO.println("No dungeons discovered.");
        }
    }

    public void enterDungeonMenu() {
        listDungeons();
        IO.println("Enter dungeon by typing its name");

        Dungeon dungeon = dungeonService.findDungeonByName(IO.readln("> "));
        if (dungeon != null) {
            if (!dungeon.getMonsters().isEmpty()) {
                game.enterDungeon(dungeon);
            } else {
                IO.println("Dungeon has already been cleared.");
            }
        } else {
            IO.println("Something went wrong when entering dungeon.");
        }
    }

    public void dungeonTutorial() {
        IO.print("""
                It seems like this is your first time entering a dungeon!
                Here's the basics:
                When you enter a dungeon, all monsters in existing
                within that dungeon will be listed for you to see.
                You will have to fight your way through all
                monsters in order complete the dungeon.
                You will always fight a single monster at a time.
                Early, you will only be able to choose between Attack and Run.
                More combat options will be available as you progress.
                You always have to type what you want to do.
                'attack' to initiate an attack, and 'run' to attempt to escape.
                To start the dungeon after entering it, simply type 'start'.
                Now, to continue into the dungeon, just type 'continue'
                """);
        String input = IO.readln("> ");
        if (!input.equalsIgnoreCase("continue")) {
            dungeonTutorial();
        }
        GameData.player.setFirstTimeDungeon(false);
    }

    public void listPlayerSkills(Character player) {
        if (!player.getSkills().isEmpty()) {
            IO.println("- Learned Skills -");
            for (Skill skill : player.getSkills()) {
                IO.println(skill);
            }
        } else {
            IO.println("You have not learned any skills.");
        }
    }


}
