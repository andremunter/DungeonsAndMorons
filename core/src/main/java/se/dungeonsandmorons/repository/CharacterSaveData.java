package se.dungeonsandmorons.repository;

import se.dungeonsandmorons.model.character.Kharacter;

public record CharacterSaveData(Kharacter player, String lastTownVisited) {}
