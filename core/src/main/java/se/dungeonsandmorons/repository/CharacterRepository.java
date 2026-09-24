package se.dungeonsandmorons.repository;

import se.dungeonsandmorons.model.character.Kharacter;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository {
    void save(Kharacter character, String lastTownVisited);
    Optional<CharacterSaveData> findByName(String name);
    List<CharacterSummary> findAllSummaries();
    boolean deleteByName(String name);
    boolean existsByName(String name);

}
