import org.junit.jupiter.api.Test;
import se.dungeonsandmorons.SaveRepository;
import se.dungeonsandmorons.model.character.Character;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadCharacterTest {

    @Test
    public void shouldReturnCharacterByName() {

        // Arrange
        Character player;

        // Act
        player = SaveRepository.load("Gucki");

        // Assert
        assertEquals("Gucki", player.getName());
    }

}
