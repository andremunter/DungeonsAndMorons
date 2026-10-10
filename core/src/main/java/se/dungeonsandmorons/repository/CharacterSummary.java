package se.dungeonsandmorons.repository;

public record CharacterSummary(
        String name,
        String characterClass,
        int level,
        String town,
        int health,
        int maxHealth,
        int mana,
        int maxMana,
        int armor,
        int strength,
        int agility,
        int intelligence,
        int vitality
) {
    @Override
    public String toString() {
        return String.format("- %s, %s, level: %d", name, characterClass, level, town);
    }

}
