package se.dungeonsandmorons.main;

import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.npc.Bird;
import se.dungeonsandmorons.model.npc.MonsterRarity;
import se.dungeonsandmorons.model.npc.Npc;

import java.util.ArrayList;
import java.util.List;



public class Initialization {


    public void createDungeon() {
        List<Npc> monsters = new ArrayList<>();
        monsters.add(new Bird("Parrot", 10, 100, MonsterRarity.NORMAL));
        monsters.add(new Bird("Parrot", 10, 100, MonsterRarity.MAGIC));
        monsters.add(new Bird("Parrot", 10, 100, MonsterRarity.RARE));

        GameData.dungeons.add(new Dungeon("Jungle", "Le jongel of only perot", 1 ,monsters));
    }

}