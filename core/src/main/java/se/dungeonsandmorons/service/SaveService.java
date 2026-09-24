package se.dungeonsandmorons.service;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.repository.CharacterRepository;
import se.dungeonsandmorons.repository.CharacterSummary;

import java.util.List;

public class SaveService {
    private final CharacterRepository repository;

    public SaveService(CharacterRepository repository) {
        this.repository = repository;
    }

    public void saveGame(GameContext context) {
        String townName = context.getActiveTown() != null
                ? context.getActiveTown().getName()
                : "Densewood";

        repository.save(context.getPlayer(), townName);
    }

    public List<CharacterSummary> getAvailableSaves() {
        return repository.findAllSummaries();
    }

    public boolean loadGame(GameContext context, String playerName) {

    }

}
