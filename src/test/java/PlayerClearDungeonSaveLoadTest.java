import org.junit.jupiter.api.Test;
import se.dungeonsandmorons.SaveRepository;
import se.dungeonsandmorons.main.Game;
import se.dungeonsandmorons.main.GameData;
import se.dungeonsandmorons.main.Initialization;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.npc.MonsterRarity;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.service.DungeonService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PlayerClearDungeonSaveLoadTest {

    @Test
    public void shouldSaveClearedDungeon() {

        // Arrange
        Game game = new Game();
        DungeonService dungService = new DungeonService();
        Character player = new Warrior("Test");
        Initialization init = new Initialization();
        List<Npc> npcs = new ArrayList<>();

        // Act
        npcs.add(new Dragon("Test", 10, 10, MonsterRarity.NORMAL));
        Dungeon dungeon = new Dungeon("TestDung", "", 1, npcs);
        player.clearDungeon("TestDung");

        SaveRepository.save(player);
        player = null;
        player = SaveRepository.load("Test");

        Dungeon clearedDungeon = null;
        for (Dungeon dung : player.getClearedDungeons()) {
            if (clearedDungeon.getName().equalsIgnoreCase("TestDung")) {
                clearedDungeon = dung;
            }
        }

        // Assert

        assertEquals("TestDung", clearedDungeon.getName());



    }

    @Test
    public void shouldLoadClearedDungeon() {

    }

}
