package se.dungeonsandmorons.model.monster;

import java.util.List;

public record MonsterTemplate(
        String id,
        String name,
        int baseHealth,
        int baseArmor,
        int baseExp,
        List<MonsterAction> actions
) implements MonsterFactory {

    public MonsterTemplate {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Template ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Monster name cannot be null or blank");
        }
        if (baseHealth <= 0) {
            throw new IllegalArgumentException("Base health must be positive, received: " + baseHealth);
        }
        if (baseArmor < 0) {
            throw new IllegalArgumentException("Base armor cannot be negative, received: " + baseArmor);
        }
        if (baseExp < 0) {
            throw new IllegalArgumentException("Base EXP cannot be negative, received: " + baseExp);
        }
        if (actions == null || actions.isEmpty()) {
            throw new IllegalArgumentException("Monster template must define at least one action");
        }

        actions = List.copyOf(actions);
    }

    @Override
    public Monster create(int level, MonsterRarity rarity) {
        return new Monster(this, level, rarity);
    }

    public Monster create(int level) {
        return create(level, MonsterRarity.NORMAL);
    }

}
