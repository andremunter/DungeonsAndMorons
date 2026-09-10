package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.exception.AttackingDeadNpcException;
import se.dungeonsandmorons.exception.SkillNotLearnedException;
import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;
import se.dungeonsandmorons.model.skill.SkillRegistry;

public class Warrior extends Character {

    public Warrior(String name) {
        super(name);
        this.strengthPerLevel = 5;
        this.agilityPerLevel = 3;
        this.intelligencePerLevel = 1;
        this.vitalityPerLevel = 3;

        learnSkill(SkillRegistry.get("Basic Melee Attack"));
    }

    public Warrior(String name, int level) {
        super(name, level);
    }

    public Warrior(int id, String name, int level, int experience, int strength, int agility, int intelligence, int vitality, int currentHealth, int maxHealth, int currentMana, int maxMana) {
        super(id, name, level, experience, strength, agility, intelligence, vitality, currentHealth, maxHealth, currentMana, maxMana);
    }

    @Override
    public CharacterClass getCharacterClass() {
        return CharacterClass.WARRIOR;
    }

    @Override
    public void attack(Targetable target, Skill skill) {

        if (target.getCurrentHealth() < 1) {
            throw new AttackingDeadNpcException();
        }

        if (skill == null || !isSkillLearned(skill)) {
            throw new SkillNotLearnedException("Trying to cast a skill that is not learned!");
        }

        int attributeBase = 0;
        switch (skill.getScalingAttribute()) {
            case Attribute.STRENGTH -> attributeBase = this.getStrength();
            case Attribute.AGILITY -> attributeBase = this.getAgility();
            case Attribute.INTELLIGENCE -> attributeBase = this.getIntelligence();
            case Attribute.VITALITY -> attributeBase = this.getVitality();
        }


        if (getCurrentMana() < skill.getResourceCost()) {
            IO.println("Not enough mana to cast " + skill.getName());
            return;
        }

        setCurrentMana(getCurrentMana() - skill.getResourceCost());
        int skillDamage = (skill.getBaseDamage() * Math.max(1,(attributeBase * skill.getAttributeDamageMultiplier())));

        IO.println(String.format("%s used `%s` and dealt %d damage to %s.", getName(), skill.getName(), skillDamage, target.getName()));
        target.takeDamage(skillDamage, this);
    }

}
