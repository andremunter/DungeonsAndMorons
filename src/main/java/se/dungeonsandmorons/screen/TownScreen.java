package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;

public class TownScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.println("- Town -");
        IO.println("Type 'help' for available commands.");
    }

    @Override
    public void handleInput(String input, GameContext context) {
        switch (input.trim().toLowerCase()) {
            case "talk"         -> {
                // TODO: blala talk contains <NpcName> find Npc, trigger blalalal
                context.getStateMachine().transition(GameEvent.INTERACT_NPC);
            }
            case "stats"        -> context.getPlayer().getStats();
            case "skills"       -> context.getSkillService().listSkills(context.getPlayer());
            case "dungeon", "explore dungeons", "dungeons"
                                -> context.getStateMachine().transition(GameEvent.SELECT_DUNGEON);
            case "save"         -> context.getSaveRepository().save(context);
            case "help"         -> displayHelp();
            case "main menu"    -> context.getStateMachine().transition(GameEvent.LOAD_GAME);
            default             -> IO.println("Unknown town command. Type 'help' for options.");
        }
    }

    public void displayHelp() {
        IO.print("""
            Available commands:
            stats              - Shows player stats
            skills             - Lists learned skills
            dungeons           - Lists available dungeons
            save               - Saves current character progress
            help               - Lists all available commands
            main menu          - Goes back to Main Menu
            """);
    }
}
