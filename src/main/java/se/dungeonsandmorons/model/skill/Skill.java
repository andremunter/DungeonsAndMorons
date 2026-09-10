package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Skill {
    private final int id;
    private final String name;
    private final int resourceCost;
    private final int baseDamage;
    private final Attribute scalingAttribute;
    private final int attributeDamageMultiplier;
    private final TargetType targetType;
    private final Set<SkillTag> tags;
    private final Set<CharacterClass> allowedClasses;

    private static int nextId = 0;

    public Skill(String name, int baseDamage, int resourceCost,
                 Attribute attribute, int attributeDamageMultiplier,
                 TargetType targetType, Set<SkillTag> tags, CharacterClass... classes) {
        this.id = nextId;
        nextId++;
        this.name = name;
        this.resourceCost = resourceCost;
        this.baseDamage = baseDamage;
        this.scalingAttribute = attribute;
        this.attributeDamageMultiplier = attributeDamageMultiplier;
        this.targetType = targetType;
        this.tags = tags;
        this.allowedClasses = classes.length > 0
                ? Collections.unmodifiableSet(EnumSet.of(classes[0], classes))
                : Collections.emptySet();
    }

    public boolean canBeLearnedBy(CharacterClass characterClass) {
        return allowedClasses.isEmpty() || allowedClasses.contains(characterClass);
    }

    public boolean hasTag(SkillTag tag) {
        return tags.contains(tag);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getResourceCost() { return resourceCost; }
    public int getBaseDamage() { return baseDamage; }
    public Attribute getScalingAttribute() { return scalingAttribute; }
    public int getAttributeDamageMultiplier() { return attributeDamageMultiplier; }
    public TargetType getTargetType() { return targetType; }
    public Set<SkillTag> getTags() { return tags; }

    @Override
    public String toString() {
        return String.format("Skill{id=%d, name=%s, baseDamage=%d, resourceCost=%d, attribute=%s, attributeDamageMultiplier=%d}",
                id, name, baseDamage, resourceCost, scalingAttribute, attributeDamageMultiplier);
    }

}
