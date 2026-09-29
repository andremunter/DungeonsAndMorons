package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.combat.CombatAbility;
import se.dungeonsandmorons.model.skill.SkillTag;
import se.dungeonsandmorons.model.skill.TargetType;

import java.util.Objects;
import java.util.Set;

public record MonsterAction(
        String name,
        int baseDamage,
        int weight,
        Set<SkillTag> tags,
        String flavorText
) implements CombatAbility {

    public MonsterAction {
        Objects.requireNonNull(name, "Action name cannot be null");
        Objects.requireNonNull(flavorText, "Flavor text cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (baseDamage < 0) {
            throw new IllegalArgumentException("Base damage cannot be negative: " + baseDamage);
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Action weight must be strictly positive: " + weight);
        }

        tags = (tags == null) ? Set.of() : Set.copyOf(tags);
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SINGLE_TARGET;
    }


}
