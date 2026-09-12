package se.dungeonsandmorons.game;

import se.dungeonsandmorons.SaveRepository;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.skill.SkillService;
import se.dungeonsandmorons.service.DungeonService;

public class GameContext {
    private Character player;
    private Dungeon activeDungeon;
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

    public Character getPlayer() { return player; }
    public void setPlayer(Character player) { this.player = player; }

    public Dungeon getActiveDungeon() { return activeDungeon; }
    public void setActiveDungeon(Dungeon dungeon) { this.activeDungeon = dungeon; }

    public GameStateMachine getStateMachine() { return stateMachine; }
    public DungeonService getDungeonService() { return dungeonService; }
    public SkillService getSkillService() { return skillService; }
    public SaveRepository getSaveRepository() { return saveRepository; }


}
