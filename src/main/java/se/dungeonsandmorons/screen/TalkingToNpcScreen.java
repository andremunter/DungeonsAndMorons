package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.npc.Npc;

public class TalkingToNpcScreen implements Screen {

    @Override
    public void display(GameContext context) {
        Npc npc = context.getActiveNpc();
        IO.println(String.format("- Talking to %s (%s) -", npc.name(), npc.role()));
        IO.println(String.format("'%s'", npc.dialogue()));
        IO.print("""
                Options: leave | trade
                """);
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {
            case "leave", "back"    -> {
                context.setActiveNpc(null);
                context.getStateMachine().transition(GameEvent.LEAVE_NPC);
            }
            case "trade"            -> {
                IO.println("TRADE NOT IMPLEMENTED");
            }
            default -> IO.println("Unkown command.");
        }

    }
}

