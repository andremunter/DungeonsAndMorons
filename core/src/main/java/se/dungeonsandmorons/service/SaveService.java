package se.dungeonsandmorons.service;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.repository.CharacterRepository;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.town.TownRegistry;

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

    public boolean loadGame(GameContext context, String playerName) {
        return repository.findByName(playerName).map(saveData -> {
            context.setPlayer(saveData.player());
            String town = saveData.lastTownVisited() != null
                    ? saveData.lastTownVisited()
                    : "Densewood";
            context.setActiveTown(TownRegistry.get(town));
            return true;
        }).orElse(false);
    }

    public List<CharacterSummary> getAvailableSaves() {
        return repository.findAllSummaries();
    }

}
