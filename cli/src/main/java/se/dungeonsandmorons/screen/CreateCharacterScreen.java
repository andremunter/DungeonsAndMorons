package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.town.TownRegistry;

public class CreateCharacterScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println("""
        
        === Character Creation ===
        
        Classes: Warrior | Magician | Archer
        Usage:      create <name> <class>
        Example:    create Cliff Hanger Warrior
        
        Type 'back' to return to Main Menu.
        """);
    }

    @Override
    public void handleInput(String input, GameContext context) {
        String trimmed = input.trim();

        if (trimmed.equalsIgnoreCase("back")) {
            context.getStateMachine().transition(GameEvent.CANCEL);
            return;
        }

        if (!trimmed.toLowerCase().startsWith("create ")) {
            IO.println("Invalid format. Use: create <name> <class>");
            return;
        }

        String payload = trimmed.substring("create ".length()).trim();

        int lastSpaceIndex = payload.lastIndexOf(' ');
        if (lastSpaceIndex == -1) {
            IO.println(String.format("Missing class! Example: create %s Warrior", payload));
            return;
        }

        String characterName = payload.substring(0, lastSpaceIndex).trim();
        String characterClass = payload.substring(lastSpaceIndex + 1).trim();

        if (characterName.isBlank()) {
            IO.println("Character name cannot be blank.");
            return;
        }

        if (context.getSaveService().saveExists(characterName)) {
            IO.println(String.format("Character with name '%s' already exists!", characterName));
            return;
        }

        switch (characterClass.toLowerCase()) {
            case "warrior"  -> {
                context.setPlayer(new Kharacter(characterName, CharacterClass.WARRIOR));
                context.setActiveTown(TownRegistry.get("Densewood"));
                IO.println(String.format("Character created: %s the Warrior!", characterName));
                context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            }

            case "archer"  -> {
                context.setPlayer(new Kharacter(characterName, CharacterClass.ARCHER));
                context.setActiveTown(TownRegistry.get("Densewood"));
                IO.println(String.format("Character created: %s the Archer!", characterName));
                context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            }

            case "magician"  -> {
                context.setPlayer(new Kharacter(characterName, CharacterClass.MAGICIAN));
                context.setActiveTown(TownRegistry.get("Densewood"));
                IO.println(String.format("Character created: %s the Magician!", characterName));
                context.getStateMachine().transition(GameEvent.ENTER_TOWN);
            }
            default         -> IO.println(String.format("Unknown class '%s'.", characterClass));
        }

    }


}