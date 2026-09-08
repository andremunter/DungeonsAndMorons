package se.dungeonsandmorons.model.npc;

@FunctionalInterface
public interface NpcFactory {
    Npc create(int level, MonsterRarity rarity);
}
