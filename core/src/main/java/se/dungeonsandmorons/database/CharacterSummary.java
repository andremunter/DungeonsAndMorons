package se.dungeonsandmorons.database;

public record CharacterSummary(String name, String characterClass, int level) {
    @Override
    public String toString() {
        return String.format("- %s, %s, level: %d", name, characterClass, level);
    }

}
