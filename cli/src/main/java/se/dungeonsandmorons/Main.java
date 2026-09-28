package se.dungeonsandmorons;

import se.dungeonsandmorons.repository.PostgresCharacterRepository;
import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameLoop;
import se.dungeonsandmorons.game.GameStateMachine;
import se.dungeonsandmorons.service.SkillService;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.service.SaveService;

public class Main {

    void main(String[] args) {

        GameLoop gameLoop = new GameLoop(
                new GameContext(
                        new GameStateMachine(),
                        new DungeonService(),
                        new SkillService(),
                        new SaveService(new PostgresCharacterRepository())
                ));
        gameLoop.run();

    }

}
