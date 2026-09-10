import org.junit.jupiter.api.Test;
import se.dungeonsandmorons.model.monster.Dragon;
import se.dungeonsandmorons.model.monster.MonsterRarity;
import se.dungeonsandmorons.model.monster.Monster;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NpcRarityTest {

    @Test
    public void rareDragonShouldHaveScaledStatsRoundedToInt() {
        // Arrange & Act
        Monster rareDragon = new Dragon("Dregor", 10, 100, 20, MonsterRarity.RARE);

        // Assert
        assertEquals(140, rareDragon.getMaxHealth());
        assertEquals(30, rareDragon.getDeathExperience());
    }

}
