package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.combat.CombatService;

public class CombatScreen implements Screen {

     private final CombatService combatService = new CombatService();

    @Override
    public void display(GameContext context) {
        IO.println(String.format("- Encountered %s!", context.getActiveDungeon().getMonsters().getFirst()));
        IO.println("Type 'help' for available commands.");
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {

        }
    }
}
