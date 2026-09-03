package se.dungeonsandmorons.main;

import se.dungeonsandmorons.TurnHandler;
import se.dungeonsandmorons.model.npc.Bird;
import se.dungeonsandmorons.model.npc.Dragon;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.npc.Npc;

import java.util.ArrayList;
import java.util.List;

import se.dungeonsandmorons.model.combat.CombatService;

public class Main {


    private final Game game = new Game();


    void main(String[] args) {

        game.runGame();


    }

}
