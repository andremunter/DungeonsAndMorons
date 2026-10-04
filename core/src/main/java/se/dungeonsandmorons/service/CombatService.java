package se.dungeonsandmorons.service;


import se.dungeonsandmorons.combat.CombatAction;
import se.dungeonsandmorons.combat.CombatEngine;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.model.skill.Skill;
import se.dungeonsandmorons.model.skill.SkillRegistry;
import se.dungeonsandmorons.session.GameSession;

public class CombatService {

    public void startCombat(GameSession session, Monster monster) {
        if (session.isInCombat()) {
            throw new IllegalStateException("Cannot start a new battle while already in combat.");
        }

        CombatEngine engine = new CombatEngine(session.getPlayer(), monster);
        engine.startRound();
        session.startCombat(engine);
    }

    public CombatEngine executeTurn(GameSession session, CombatAction action, String skillName) {
        CombatEngine engine = session.getActiveCombat();
        if (engine == null) {
            throw new IllegalStateException("No active combat found for this session.");
        }

        Skill skill = (skillName != null) ? SkillRegistry.get(skillName) : null;
        engine.executePlayerAction(action, skill);

        if (engine.getState().isTerminal()) {
            session.endCombat();
        }

        return engine;
    }
}
