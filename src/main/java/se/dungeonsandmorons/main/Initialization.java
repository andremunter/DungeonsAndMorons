package se.dungeonsandmorons.main;

import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.npc.*;

import java.util.ArrayList;
import java.util.List;



public class Initialization {


    public void createDungeons() {

        Dungeon rotwormCave = new Dungeon("Rotworm Cave", "Cave of only rottenwurmen", 1);
        GameData.dungeons.add(rotwormCave);

        List<NpcFactory> caveRecipes = List.of(
                (lvl, rarity) -> new Rotworm("Rotworm", lvl, 40, 20, rarity),
                (lvl, rarity) -> new Rotworm("Rotworm", lvl, 40, 20, rarity)
        );

        rotwormCave.populateDungeon(caveRecipes);

    }

}