package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;

import java.util.*;

public class SkillRegistry {

    private static final Map<String, Skill> SKILLS = new HashMap<>();


    static {
        // WARRIOR SKILLS
        register(new Skill("Basic Melee Attack", 5, 0,
                Attribute.STRENGTH, 1, TargetType.SINGLE_TARGET,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL), CharacterClass.WARRIOR));

        register(new Skill("Strike", 9, 5,
                Attribute.STRENGTH, 1, TargetType.SINGLE_TARGET,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL), CharacterClass.WARRIOR));

         register(new Skill("Raise Shield", 0, 5,
                Attribute.STRENGTH, 1, TargetType.SELF,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL), CharacterClass.WARRIOR));

        // MAGE SKILLS
        register(new Skill("Basic Magic Attack", 4, 0,
                Attribute.STRENGTH, 1, TargetType.SINGLE_TARGET,
                Set.of(SkillTag.RANGED, SkillTag.FIRE), CharacterClass.MAGICIAN));

    }


    public static void register(Skill skill) {
        SKILLS.put(skill.getName().toLowerCase(), skill);
    }

    public static Skill get(String name) {
        return SKILLS.get(name.toLowerCase());
    }

    public static Map<String, Skill> getAll() {
        return Collections.unmodifiableMap(SKILLS);
    }

}
