package se.dungeonsandmorons.screen;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameEvent;
import se.dungeonsandmorons.model.npc.Npc;

public class TownScreen implements Screen {

    @Override
    public void display(GameContext context) {
        IO.print(String.format("""
                
                === %s ===
                
                """, context.getActiveTown().getName()));

        for (Npc npc : context.getActiveTown().getNpcs()) {
            IO.println(String.format("%s - %s", npc.name(), npc.role()));
        }

        IO.print("""
                
                Type 'help' for available commands.
                
                """);
    }

    @Override
    public void handleInput(String input, GameContext context) {

        switch (input.trim().toLowerCase()) {
            case String s when s.startsWith("talk ") -> {
                // TODO: blala talk contains <NpcName> find Npc, trigger blalalal

                String target = s.substring("talk ".length()).trim();

                Npc targetNpc = context.getActiveTown().findNpc(target);
                if (targetNpc != null) {
                    context.setActiveNpc(targetNpc);
                    context.getStateMachine().transition(GameEvent.INTERACT_NPC);
                } else {
                    IO.println(String.format("Could not find '%s'.", target));
                }

            }
            case "talk"         -> IO.println("Specify who to talk to. Example: 'talk Flint Blunt' or 'talk blacksmith'");
            case "stats"        -> context.getStateMachine().transition(GameEvent.OPEN_STAT_MANAGEMENT);
            case "skills"       -> context.getSkillService().listSkills(context.getPlayer());
            case "dungeon", "explore dungeons", "dungeons"
                                -> context.getStateMachine().transition(GameEvent.SELECT_DUNGEON);
            case "save"         -> context.getSaveService().saveGame(context.getPlayer(), context.getActiveTown().getName());
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
