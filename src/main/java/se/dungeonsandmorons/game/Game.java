package se.dungeonsandmorons.game;

import se.dungeonsandmorons.model.combat.CombatService;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.userinterface.UserInterface;

public class Game {

    private UserInterface userInterface;
    public static CombatService combatService;

   public void startGame(UserInterface userInterface, CombatService cService) {
       this.userInterface = userInterface;
       combatService = cService;
       userInterface.mainMenu(this);
   }

    public void enterDungeon(Dungeon dungeon) {
       /*
       if (GameData.PLAYER.isFirstTimeDungeon()) {
            userInterface.dungeonTutorial();
        }
        */

        while (dungeon != null && !dungeon.getMonsters().isEmpty()) {
            IO.println(dungeon);
            IO.println("Monsters in dungeon: " + dungeon.getMonsters().size());
            if (!dungeon.getMonsters().isEmpty()) {
                int monsterIndex = 1;
                for (Monster monster : dungeon.getMonsters()) {
                    IO.println(String.format("%d. %s, HP: %d, Exp Reward: %d", monsterIndex, monster, monster.getCurrentHealth(), monster.getDeathExperience()));
                    monsterIndex++;
                }
            }

            if (combatService.runCombatLoop(dungeon.getMonsters().getFirst(), GameData.PLAYER)) {
                dungeon.getMonsters().removeFirst();
            }

            if (dungeon.getMonsters().isEmpty()) {
                GameData.PLAYER.addClearedDungeon(dungeon);
                IO.println(String.format("%s cleared!", dungeon.getName()));
            }
        }
    }



}
