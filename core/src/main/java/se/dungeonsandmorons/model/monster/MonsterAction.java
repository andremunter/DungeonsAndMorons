package se.dungeonsandmorons.model.monster;

import se.dungeonsandmorons.combat.CombatAbility;
import se.dungeonsandmorons.model.skill.SkillTag;
import se.dungeonsandmorons.model.skill.TargetType;

import java.util.Set;

public class MonsterAction implements CombatAbility {
    private final String name;
    private final int damage;
    private final int weight;
    private final Set<SkillTag> tags;
    private final String flavorText;

    public MonsterAction(String name, int damage, int weight, Set<SkillTag> tags, String combatFlavorText) {
        this.name = name;
        this.damage = damage;
        this.weight = weight;
        this.tags = tags;
        this.flavorText = combatFlavorText;
    }

    @Override public String getName() { return name; }
    @Override public TargetType getTargetType() { return TargetType.SINGLE_TARGET; }
    @Override public Set<SkillTag> getTags() { return tags; }

    public int damage() { return damage; }
    public int weight() { return weight; }
    public String flavorText() { return flavorText; }

}
