package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.combat.CombatAbility;

import java.util.Set;

public class NpcAction implements CombatAbility {
    private final String name;
    private final int damage;
    private final Set<SkillTag> tags;
    private final String combatFlavorText;

    public NpcAction(String name, int damage, Set<SkillTag> tags, String combatFlavorText) {
        this.name = name;
        this.damage = damage;
        this.tags = tags;
        this.combatFlavorText = combatFlavorText;
    }

    @Override public String getName() { return name; }
    @Override public TargetType getTargetType() { return TargetType.SINGLE_TARGET; }
    @Override public Set<SkillTag> getTags() { return tags; }

    public int getDamage() { return damage; }
    public String getCombatFlavorText() { return combatFlavorText; }

}
