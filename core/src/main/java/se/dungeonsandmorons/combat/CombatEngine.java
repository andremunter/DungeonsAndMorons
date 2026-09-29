package se.dungeonsandmorons.combat;

import lombok.Getter;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.model.monster.MonsterAction;
import se.dungeonsandmorons.model.skill.Skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class CombatEngine {
    private final Kharacter player;
    private final Monster monster;
    private CombatState state;
    private int roundCount;
    private final List<String> combatLog = new ArrayList<>();

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
            case ATTACK, CAST -> {
                int damageDealt = player.attack(monster, skill);
                combatLog.add(String.format("You used %s on %s for %d damage.",
                        skill != null ? skill.name() : "Basic Attack", monster.getName(), damageDealt
                        ));
            }
            case RUN    -> {
                // TODO: Some kind of escape odds calculation?
                this.state = CombatState.ESCAPED;
                combatLog.add("You escaped from battle!");
                return;
            }
        }

        if (!monster.isAlive()) {
            // TODO: CombatState.VICTORY -> CombatState.LOOT? How to handle looting hmm?
            this.state = CombatState.VICTORY;
            combatLog.add(String.format("You defeated %s and earned %d experience",
                    monster.getName(), monster.getExpOnDeath()));
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

            int rawDamage = monster.calculateActionDamage(action);
            int finalDamage = player.takeDamage(rawDamage, monster);
            combatLog.add(String.format("%s used %s for %d damage!",
                    monster.getName(), action.name(), finalDamage));
        }

        if (!player.isAlive()) {
            // TODO: Some kind of evaluation of loss of exp, loot, etc.
            this.state = CombatState.DEFEAT;
            combatLog.add("You were slain in battle...");
        } else {
            this.roundCount++;
            this.state = CombatState.ROUND_START;
            startRound();
        }
    }

    public List<String> getCombatLog() { return Collections.unmodifiableList(combatLog); }
}
