package se.dungeonsandmorons.game;

import se.dungeonsandmorons.repository.SaveRepository;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.combat.CombatEngine;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.npc.Npc;
import se.dungeonsandmorons.model.skill.SkillService;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.town.Town;

public class GameContext {
    private Kharacter player;
    private Town activeTown;
    private Npc activeNpc;
    private Dungeon activeDungeon;
    private CombatEngine activeCombat;
    private final GameStateMachine stateMachine;

    private final DungeonService dungeonService;
    private final SkillService skillService;
    private final SaveRepository saveRepository;

    public GameContext(GameStateMachine stateMachine,
                       DungeonService dungeonService,
                       SkillService skillService,
                       SaveRepository saveRepository) {
        this.stateMachine = stateMachine;
        this.dungeonService = dungeonService;
        this.skillService = skillService;
        this.saveRepository = saveRepository;
    }

    public Kharacter getPlayer() { return player; }
    public void setPlayer(Kharacter player) { this.player = player; }

    public Town getActiveTown() { return activeTown; }
    public void setActiveTown(Town activeTown) { this.activeTown = activeTown; }

    public Npc getActiveNpc() { return activeNpc; }
    public void setActiveNpc(Npc activeNpc) { this.activeNpc = activeNpc; }

    public Dungeon getActiveDungeon() { return activeDungeon; }
    public void setActiveDungeon(Dungeon dungeon) { this.activeDungeon = dungeon; }

    public CombatEngine getActiveCombat() { return activeCombat; }
    public void setActiveCombat(CombatEngine activeCombat) { this.activeCombat = activeCombat; }

    public GameStateMachine getStateMachine() { return stateMachine; }
    public DungeonService getDungeonService() { return dungeonService; }

    public SkillService getSkillService() { return skillService; }
    public SaveRepository getSaveRepository() { return saveRepository; }


}
