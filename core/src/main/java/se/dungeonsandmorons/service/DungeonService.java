package se.dungeonsandmorons.service;

import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.dungeon.DungeonRegistry;
import se.dungeonsandmorons.model.dungeon.DungeonRun;
import se.dungeonsandmorons.session.GameSession;

import java.util.List;

public class DungeonService {

    private final SaveService saveService;


    public DungeonService(SaveService saveService) {
        this.saveService = saveService;
    }

    public void handlePlayerDeath(GameSession session, String respawnTownId) {
        DungeonRun run = session.getActiveRun();
        if (run == null) {
            return;
        }

        /// Example: 50% loss of pending experience on death
        int finalExp = run.applyDeathPenalty(0.50);

        Kharacter player = session.getPlayer();
        player.addExperience(finalExp);

        player.setCurrentHealth(player.getMaxHealth());

        saveService.saveGame(player, respawnTownId);
        session.endDungeonRun();
    }

    public void leaveDungeon(GameSession session, String currentTown) {
        DungeonRun run = session.getActiveRun();
        if (run == null) {
            throw new IllegalStateException("Not currently in a dungeon");
        }

        Kharacter player = session.getPlayer();
        player.addExperience(run.getPendingExp());

        if (run.isDungeonCleared()) {
            player.addClearedDungeon(run.getDungeon().getName());
        }

        saveService.saveGame(player, currentTown);
        session.clearDungeon();
    }

    public List<String> getAvailableDungeonNames() {
        return DungeonRegistry.getAllDungeonIds();
    }

    public Dungeon findDungeonByName(String name) {
        Dungeon dungeon = DungeonRegistry.create(name);
        if (dungeon == null) {
            System.out.println("Could not find specified dungeon.");
            return null;
        }
        return dungeon;
    }

}
