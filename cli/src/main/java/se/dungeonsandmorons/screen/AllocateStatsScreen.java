package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.character.Attribute;

public class AllocateStatsScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.print("""
                
                === STAT MANAGEMENT ===
                
                To allocate stats:
                allocate <str|agi|int|vit> <amount>
                
                Stats to show player stats
                
                """);

    }

    @Override
    public void handleInput(String input, GameContext context) {

        switch (input.toLowerCase()) {
            case "stats" -> {
                context.getPlayer().getStats();
                return;
            }
            case "back", "cancel", "town" -> {
                context.getStateMachine().transition(GameEvent.CANCEL);
                return;
            }
        }

        String[] parts = input.trim().split("\\s+");

        if (parts.length != 3 || !parts[0].equalsIgnoreCase("allocate")) {
            IO.println("Usage: allocate <str|agi|int|vit> <amount>");
            return;
        }

        Attribute attribute = switch (parts[1]) {
            case "str", "strength"      -> Attribute.STRENGTH;
            case "agi", "agility"       -> Attribute.AGILITY;
            case "int", "intelligence"  -> Attribute.INTELLIGENCE;
            case "vit", "vitality"      -> Attribute.VITALITY;
            default                     -> null;
        };

        if (attribute != null) {
            IO.println("Invalid attribute. Choose str, agi, int or vit.");
            return;
        }

        try {
            int points = Integer.parseInt(parts[2]);
            boolean success = context.getPlayer().allocateStat(attribute, points);

            if (success) {
                IO.println(String.format("Allocated %d points to %s. Remining points: %d",
                        points, attribute, context.getPlayer().getUnallocatedStatPoints()));
            } else {
                IO.println("Not enough unallocated stat points! Available: " + context.getPlayer().getUnallocatedStatPoints());
            }
        } catch (NumberFormatException e) {
            IO.println("Amount must be a valid number.");
        }

    }

}
