package se.dungeonsandmorons.model.character;

import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

public class Warrior extends Character {

    public Warrior(String name) {
        super(name);
        this.strengthPerLevel = 5;
        this.agilityPerLevel = 3;
        this.intelligencePerLevel = 1;
        this.vitalityPerLevel = 3;

        this.skills.add(new Skill("Swing", "swosch", 10, Attribute.STRENGTH, 2, 0));
    }

    public Warrior(String name, int level) {
        super(name, level);
    }

    public Warrior(String name, int level, int experience, int strength, int agility, int intelligence, int vitality, int currentHealth, int maxHealth, int currentMana, int maxMana) {
        super(name, level, experience, strength, agility, intelligence, vitality, currentHealth, maxHealth, currentMana, maxMana);
    }

    @Override
    public void attack(Targetable target, Skill skill) {

        if (target.getCurrentHealth() < 1) {
            IO.println(target.getName() + " is already dead.");
            return;
        }

        int attributeBase = 0;
        switch (skill.getAttribute()) {
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
