import org.junit.jupiter.api.Test;
import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.skill.Skill;

import static org.junit.jupiter.api.Assertions.*;

public class WarriorTest {

    @Test
    public void warriorShouldHaveDefaultSkill() {
        // Arrange & Act
        Character warrior = new Warrior("Test");

        // Assert
        assertNotNull(warrior.getSkills().getFirst());
        assertFalse(warrior.getSkills().isEmpty(), "Warriors starts with a basic skill 'Swing'");
    }

    @Test
    public void warriorShouldBeAbleToLearnSkill() {
        // Arrange
        Warrior warrior = new Warrior("Test");
        Skill slash = new Skill("Slash", "Schring", 5, Attribute.AGILITY, 1, 10);

        // Act
        warrior.learnSkill(slash);

        // Assert
        assertTrue(warrior.getSkills().contains(slash));
        assertEquals(2, warrior.getSkills().size(), "Warrior start with 1 default skill, adding another should make this 2.");

    }

}
