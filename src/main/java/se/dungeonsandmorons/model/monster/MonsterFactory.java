package se.dungeonsandmorons.model.monster;

@FunctionalInterface
public interface MonsterFactory {
    Monster create(int level, MonsterRarity rarity);
}
