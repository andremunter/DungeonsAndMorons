package se.dungeonsandmorons.model.combat;

import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.model.monster.MonsterAction;
import se.dungeonsandmorons.model.skill.Skill;

public class CombatEngine {
    private final Kharacter player;
    private final Monster monster;
    private CombatState state;
    private int roundCount;

    public CombatEngine(Kharacter player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.state = CombatState.ROUND_START;
        this.roundCount = 1;
    }

    public void startRound() {
        if (state != CombatState.ROUND_START) {
            throw new IllegalStateException("Cannot start round from state " + state);
        }

        // TODO: If we want DoTs or something like regeneration we can tick it here?

        this.state = CombatState.AWAITING_PLAYER_INPUT;
    }

    public void executePlayerAction(CombatAction action, Skill skill) {
        if (state != CombatState.AWAITING_PLAYER_INPUT) {
            throw new IllegalStateException("Player action forbidden in state: " + state);
        }

        this.state = CombatState.RESOLVING_PLAYER_ACTION;

        switch (action) {
            // TODO: Separate Attack and Cast. Dunno yet how. Cast should be like a magicians Firebolt or a Buff
            case ATTACK, CAST -> player.attack(monster, skill);
            case RUN    -> {
                // TODO: Some kind of escape odds calculation?
                this.state = CombatState.ESCAPED;
                return;
            }
        }

        if (!monster.isAlive()) {
            // TODO: CombatState.VICTORY -> CombatState.LOOT? How to handle looting hmm?
            this.state = CombatState.VICTORY;
        } else {
            this.state = CombatState.RESOLVING_ENEMY_ACTION;
            resolveEnemyTurn();
        }

    }

    public void resolveEnemyTurn() {
        if (state != CombatState.RESOLVING_ENEMY_ACTION) {
            throw new IllegalStateException("Illegal enemy turn in state: " + state);
        }


        if (monster.isAlive()) {
            MonsterAction action = monster.chooseAction();

            if (action == null) {
                throw new NullPointerException(String.format("%s have no actions registered.", monster.getName()));
            }

            int damage = action.damage();
            player.takeDamage(damage, monster);
            IO.println(String.format("%s used %s for %d damage", monster.getName(), action.getName(), damage));
        }

        if (!player.isAlive()) {
            // TODO: Some kind of evaluation of loss of exp, loot, etc.
            this.state = CombatState.DEFEAT;
        } else {
            this.roundCount++;
            this.state = CombatState.ROUND_START;
            startRound();
        }
    }

    public CombatState getState() { return state; }
    public int getRoundCount() { return roundCount; }
    public Kharacter getPlayer() { return player; }
    public Monster getEnemy() { return monster; }



}
