package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.combat.CombatAbility;

import java.util.Set;

public class Skill implements CombatAbility {
    private final String name;
    private final int resourceCost;
    private final int baseDamage;
    private final Attribute scalingAttribute;
    private final int attributeDamageMultiplier;
    private final TargetType targetType;
    private final Set<SkillTag> tags;
    private final Set<CharacterClass> allowedClasses;


    public Skill(String name, int baseDamage, int resourceCost,
                 Attribute attribute, int attributeDamageMultiplier,
                 TargetType targetType, Set<SkillTag> tags, Set<CharacterClass> allowedClasses) {
        this.name = name;
        this.resourceCost = resourceCost;
        this.baseDamage = baseDamage;
        this.scalingAttribute = attribute;
        this.attributeDamageMultiplier = attributeDamageMultiplier;
        this.targetType = targetType;
        this.tags = tags;
        this.allowedClasses = allowedClasses;
    }

    public boolean canBeLearnedBy(CharacterClass clazz) {
        return allowedClasses.isEmpty() || allowedClasses.contains(clazz);
    }

    public boolean hasTag(SkillTag tag) {
        return tags.contains(tag);
    }

    @Override public String name() { return name; }
    @Override public TargetType getTargetType() { return targetType; }
    @Override public Set<SkillTag> tags() { return tags; }
    public int getResourceCost() { return resourceCost; }
    public int getBaseDamage() { return baseDamage; }
    public Attribute getScalingAttribute() { return scalingAttribute; }
    public int getAttributeDamageMultiplier() { return attributeDamageMultiplier; }

    @Override
    public String toString() {
        return String.format("Skill{name=%s, baseDamage=%d, resourceCost=%d, attribute=%s, attributeDamageMultiplier=%d}",
                name, baseDamage, resourceCost, scalingAttribute, attributeDamageMultiplier);
    }

}
