package se.dungeonsandmorons.game;

import se.dungeonsandmorons.model.combat.CombatService;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.npc.Npc;
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
        if (GameData.player.isFirstTimeDungeon()) {
            userInterface.dungeonTutorial();
        }

        while (true) {
            IO.println(dungeon);
            if (!dungeon.getMonsters().isEmpty()) {
                for (Npc monsters : dungeon.getMonsters()) {
                    IO.println(monsters);
                }
            }

            if (combatService.engageCombat(dungeon.getMonsters().getFirst(), GameData.player)) {
                dungeon.getMonsters().removeFirst();
            }

            if (dungeon.getMonsters().isEmpty()) {
                GameData.player.addClearedDungeon(dungeon);
                IO.println(String.format("%s cleared!", dungeon.getName()));
                return;
            }
        }
    }



}
