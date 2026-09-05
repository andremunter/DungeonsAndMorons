import org.junit.jupiter.api.Test;
import se.dungeonsandmorons.TurnHandler;
import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.combat.PlayerCombatAction;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.npc.MonsterRarity;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.model.skill.Skill;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TurnHandlerTest {

    @Test
    public void handleTurnPlayerShouldUseDefaultSkillAndEnemyAttacksBack() {
        // Arrange
        TurnHandler th = new TurnHandler();
        Character player = new Warrior("Test");
        Npc dragon = new Dragon("Drag-on", 100, 10, MonsterRarity.NORMAL);

        // Act
        th.handleTurn(dragon, player, PlayerCombatAction.ATTACK, null);

        // Assert
        assertTrue(dragon.getCurrentHealth() < 100);
        assertTrue(player.getCurrentHealth() < player.getMaxHealth());
    }

    @Test
    public void handleTurnPlayerShouldCastAndEnemyAttacksBack() {
        // Arrange
        TurnHandler th = new TurnHandler();
        Character player = new Warrior("Test");
        Npc dragon = new Dragon("Draggson", 100, 10, MonsterRarity.NORMAL);
        Skill skill = new Skill("Thunda", "BZZZT", 5, Attribute.INTELLIGENCE, 1, 30);


        // Act
        player.learnSkill(skill);
        th.handleTurn(dragon, player, PlayerCombatAction.CAST, player.getSkills().get(1));

        // Assert
        assertTrue(player.getCurrentMana() < player.getMaxMana());
        assertTrue(dragon.isAlive());

    }

    @Test
    public void handleTurnMonsterDies() {

    }

    @Test
    public void handleTurnPlayerDies() {

    }


}
