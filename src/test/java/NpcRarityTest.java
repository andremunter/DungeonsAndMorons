import org.junit.jupiter.api.Test;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.npc.MonsterRarity;
import se.dungeonsandmorons.model.npc.Enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NpcRarityTest {

    @Test
    public void rareDragonShouldHaveScaledStatsRoundedToInt() {
        // Arrange & Act
        Enemy rareDragon = new Dragon("Dregor", 10, 100, 20, MonsterRarity.RARE);

        // Assert
        assertEquals(140, rareDragon.getMaxHealth());
        assertEquals(30, rareDragon.getDeathExperience());
    }

}
