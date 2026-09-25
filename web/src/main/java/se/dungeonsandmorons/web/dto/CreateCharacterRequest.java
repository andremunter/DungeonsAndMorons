package se.dungeonsandmorons.web.dto;

public record CreateCharacterRequest(
        String name,
        String characterClass
) {}
