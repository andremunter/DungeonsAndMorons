package se.dungeonsandmorons;

import se.dungeonsandmorons.repository.CharacterRepository;
import se.dungeonsandmorons.repository.PostgresCharacterRepository;
import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameLoop;
import se.dungeonsandmorons.game.GameStateMachine;
import se.dungeonsandmorons.service.SkillService;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.service.SaveService;

public class Main {

    void main(String[] args) {

        final CharacterRepository repository = new PostgresCharacterRepository();
        final SaveService saveService = new SaveService(repository);
        final DungeonService dungeonService = new DungeonService(saveService);

        GameLoop gameLoop = new GameLoop(
                new GameContext(
                        new GameStateMachine(),
                        dungeonService,
                        new SkillService(),
                        saveService
                ));
        gameLoop.run();

    }

}
