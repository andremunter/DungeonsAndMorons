package se.dungeonsandmorons;

import se.dungeonsandmorons.repository.SaveRepository;
import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.game.GameLoop;
import se.dungeonsandmorons.game.GameStateMachine;
import se.dungeonsandmorons.model.skill.SkillService;
import se.dungeonsandmorons.service.DungeonService;

public class Main {

    void main(String[] args) {

        GameLoop gameLoop = new GameLoop(
                new GameContext(
                        new GameStateMachine(),
                        new DungeonService(),
                        new SkillService(),
                        new SaveRepository()
                ));
        gameLoop.run();

    }

}
