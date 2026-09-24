package se.dungeonsandmorons.service;

import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.repository.CharacterRepository;
import se.dungeonsandmorons.repository.CharacterSaveData;
import se.dungeonsandmorons.repository.CharacterSummary;
import se.dungeonsandmorons.town.TownRegistry;

import java.util.List;
import java.util.Optional;

public class SaveService {
    private final CharacterRepository repository;

    public SaveService(CharacterRepository repository) {
        this.repository = repository;
    }

    public void saveGame(Kharacter player, String townName) {
        repository.save(player, townName);
    }

    public Optional<CharacterSaveData> loadGame(String playerName) {
        return repository.findByName(playerName);
    }

    public boolean deleteSave(String playerName) {
        return repository.deleteByName(playerName);
    }

    public boolean saveExists(String playerName) {
        return repository.existsByName(playerName);
    }

    public List<CharacterSummary> getAvailableSaves() {
        return repository.findAllSummaries();
    }

}
