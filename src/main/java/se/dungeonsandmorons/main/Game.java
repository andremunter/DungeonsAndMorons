package se.dungeonsandmorons.main;

import se.dungeonsandmorons.SaveRepository;
import se.dungeonsandmorons.model.combat.CombatService;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.npc.Bird;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.npc.MonsterRarity;
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

    public void listDungeons() {
        if (!gameData.dungeons.isEmpty()) {
            IO.println("- Dungeons -");
            for (Dungeon dungeon : gameData.dungeons) {
                IO.println(dungeon);
            }
        } else {
            IO.println("No dungeons discovered.");
        }
    }

    public void enterDungeon(Dungeon dungeon) {

        if (gameData.player.isFirstTimeDungeon()) {
            dungeonTutorial();
        }

        IO.println(dungeon);
        for (Npc monsters : dungeon.getMonsters()) {
            IO.println(monsters);
        }



    }

    public void combatOptions(Npc monster, Character player) {
        switch (IO.readln("> ").toLowerCase()) {
            case "attack"   -> combatService.engageCombat(monster, player);
            case "run"      -> escapeFight();
            default         -> IO.println("Wrong input.");
        }
    }

    public void escapeFight() {
        IO.println("NOT IMPLEMENTED! GL :)");
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
                Now, to continue into the dungeon, just type 'continue'
                """);
        String input = IO.readln("> ");
        if (!input.equalsIgnoreCase("continue")) {
            dungeonTutorial();
        }
        gameData.player.setFirstTimeDungeon(false);
    }

    public void enterDungeonMenu() {
        listDungeons();
        IO.println("Enter dungeon by typing its name");
        Dungeon dungeon = findDungeonByName(IO.readln("> "));
        if (dungeon != null) {
            enterDungeon(dungeon);
        } else {
            IO.println("Something went wrong when entering dungeon.");
        }
    }

    public void createDungeon() {
        List<Npc> monsters = new ArrayList<>();
        monsters.add(new Bird("Parrot", 40, 10, MonsterRarity.NORMAL));
        monsters.add(new Bird("Parrot", 40, 10, MonsterRarity.NORMAL));
        monsters.add(new Bird("Parrot", 40, 10, MonsterRarity.NORMAL));
        monsters.add(new Bird("Parrot", 40, 10, MonsterRarity.MAGIC));
        monsters.add(new Bird("Parrot", 40, 10, MonsterRarity.RARE));
        gameData.dungeons.add(new Dungeon("Jungle", "Le jongel of only perot", 1 ,monsters));
    }


    public Dungeon findDungeonByName(String name) {
        if (!gameData.dungeons.isEmpty()) {
            for (Dungeon dungeon : gameData.dungeons) {
                if (dungeon.getName().equalsIgnoreCase(name)) {
                    return dungeon;
                }
            }
        }
        IO.println("Could not find specified dungeon.");
        return null;
    }

    public void runGame() {

        while (true) {

            String input = IO.readln("> ");
            switch (input) {
                case String s when s.toLowerCase().startsWith("attack ") -> {
                    String targetName = s.substring("attack ".length()).trim();
                   // for (Npc monster : monsters) {
                    //    if (monster.getName().equalsIgnoreCase(targetName)) {

                    //        combatService.engageCombat(monster, gameData.player);
                     //   }
                   // }
                }
                case "stats" -> gameData.player.getStats();
                case "dungeons" -> enterDungeonMenu();
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
