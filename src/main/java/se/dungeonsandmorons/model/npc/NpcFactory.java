package se.dungeonsandmorons.model.npc;

@FunctionalInterface
public interface NpcFactory {
    Enemy create(int level, MonsterRarity rarity);
}
