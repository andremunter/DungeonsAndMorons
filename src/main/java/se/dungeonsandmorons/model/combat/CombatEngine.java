package se.dungeonsandmorons.model.combat;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.npc.Enemy;
import se.dungeonsandmorons.model.skill.PlayerSkill;

public class CombatEngine {
    private final Character player;
    private final Enemy enemy;
    private CombatState state;
    private int roundCount;

    public CombatEngine(Character player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
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

    public void executePlayerAction(CombatAction action, PlayerSkill skill) {
        if (state != CombatState.AWAITING_PLAYER_INPUT) {
            throw new IllegalStateException("Player action forbidden in state: " + state);
        }

        this.state = CombatState.RESOLVING_PLAYER_ACTION;

        switch (action) {
            // TODO: Separate Attack and Cast. Dunno yet how. Cast should be like a magicians Firebolt or a Buff
            case ATTACK, CAST -> player.attack(enemy, skill);
            case RUN    -> {
                // TODO: Some kind of escape odds calculation?
                this.state = CombatState.ESCAPED;
                return;
            }
        }

        if (!enemy.isAlive()) {
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


        if (enemy instanceof Attacker attacker && enemy.isAlive()) {
            // TODO: Chance-based skill choice, maybe separate Skill-system, NpcSkill/CharacterSkill
            // Example; Dragon has Swipe, Fire Breath, Incinerate. 50% chance to swipe, 30% fire breath, 20% incinerate
            attacker.attack(player, enemy.getSkills().getFirst());
        }

        if (!player.isAlive()) {
            // TODO: Some kind of evaluation of loss of exp, loot, etc.
            this.state = CombatState.DEFEAT;
        } else {
            this.roundCount++;
            this.state = CombatState.ROUND_START;
        }
    }

    public CombatState getState() { return state; }
    public int getRoundCount() { return roundCount; }
    public Character getPlayer() { return player; }
    public Enemy getEnemy() { return enemy; }



}
