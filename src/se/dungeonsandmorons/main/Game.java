package se.dungeonsandmorons.main;

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


        Character player = new Warrior("Gucki");

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
                case "help" -> {
                    IO.print("""
                            Available commands:
                            attack monsterName
                            stats
                            monsters
                            help
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
