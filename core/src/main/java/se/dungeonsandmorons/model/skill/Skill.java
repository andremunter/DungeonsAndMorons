package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.character.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.combat.CombatAbility;

import java.util.Set;

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

    public String getId() { return id; }
    public String getDescription() { return description; }
    public int getResourceCost() { return resourceCost; }
    public int getBaseDamage() { return baseDamage; }
    public Attribute getScalingAttribute() { return scalingAttribute; }
    public double getAttributeDamageMultiplier() { return attributeDamageMultiplier; }
    public Set<CharacterClass> getAllowedClasses() { return allowedClasses; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String description;
        private int resourceCost;
        private int baseDamage;
        private Attribute scalingAttribute;
        private double attributeDamageMultiplier;
        private TargetType targetType;
        private Set<SkillTag> tags;
        private Set<CharacterClass> allowedClasses;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder resourceCost(int resourceCost) { this.resourceCost = resourceCost; return this; }
        public Builder baseDamage(int baseDamage) { this.baseDamage = baseDamage; return this; }
        public Builder scalingAttribute(Attribute scalingAttribute) { this.scalingAttribute = scalingAttribute; return this; }
        public Builder attributeDamageMultiplier(double attributeDamageMultiplier) { this.attributeDamageMultiplier = attributeDamageMultiplier; return this; }
        public Builder targetType(TargetType targetType) { this.targetType = targetType; return this; }
        public Builder tags(Set<SkillTag> tags) { this.tags = tags; return this; }
        public Builder allowedClasses(Set<CharacterClass> allowedClasses) { this.allowedClasses = allowedClasses; return this; }

        public Skill build() {
            return new Skill(id, name, description, baseDamage, resourceCost, scalingAttribute, attributeDamageMultiplier, targetType, tags, allowedClasses);
        }
    }



    @Override
    public String toString() {
        return String.format("Skill{name=%s, baseDamage=%d, resourceCost=%d, attribute=%s, attributeDamageMultiplier=%.1f}",
                name, baseDamage, resourceCost, scalingAttribute, attributeDamageMultiplier);
    }

}
