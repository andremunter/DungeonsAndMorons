package se.dungeonsandmorons.service;

import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.repository.CharacterSaveData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatService {
    private final Map<UUID, CombatEngine> activeBattles = new ConcurrentHashMap<>();

    private final SaveService saveService;

    public CombatService(SaveService saveService) {
        this.saveService = saveService;
    }

}
