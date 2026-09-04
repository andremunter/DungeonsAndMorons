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

    public void runGame() {

        List<Npc> monsters = new ArrayList<>();
        monsters.add(new Dragon("Green Dragon", 11, 50));
        monsters.add(new Bird("Parrot", 199, 5));


        Character player = new Warrior("Default");

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
                    SaveRepository.load(charName);
                }
                case "help" -> {
                    IO.print("""
                            Available commands:
                            attack monsterName - Engaged in combat with specified monster
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
