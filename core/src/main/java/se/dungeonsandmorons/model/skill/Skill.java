package se.dungeonsandmorons.model.skill;

import lombok.Builder;
import lombok.Getter;
import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.combat.CombatAbility;

import java.util.Set;

@Getter
@Builder
public class Skill implements CombatAbility {
    private final String id;
    private final String name;

    private final String description;
    private final int resourceCost;
    private final int baseDamage;
    private final Attribute scalingAttribute;
    private final double attributeDamageMultiplier;
    private final TargetType targetType;
    private final Set<SkillTag> tags;
    private final Set<CharacterClass> allowedClasses;


    public Skill(String id, String name, String description,
                 int baseDamage, int resourceCost,
                 Attribute attribute, double attributeDamageMultiplier,
                 TargetType targetType, Set<SkillTag> tags, Set<CharacterClass> allowedClasses) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return String.format("Skill{name=%s, baseDamage=%d, resourceCost=%d, attribute=%s, attributeDamageMultiplier=%.1f}",
                name, baseDamage, resourceCost, scalingAttribute, attributeDamageMultiplier);
    }

}
