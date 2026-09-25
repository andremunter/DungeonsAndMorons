package se.dungeonsandmorons.repository;

public record CharacterSummary(String name, String characterClass, int level, String town) {
    @Override
    public String toString() {
        return String.format("- %s, %s, level: %d", name, characterClass, level, town);
    }

}
