package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.Attribute;
import se.dungeonsandmorons.model.character.CharacterClass;

import java.util.*;

public class SkillRegistry {

    private static final Map<String, PlayerSkill> SKILLS = new HashMap<>();


    static {
        // WARRIOR SKILLS
        register(new PlayerSkill("Basic Melee Attack", 5, 0,
                Attribute.STRENGTH, 1, TargetType.SINGLE_TARGET,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL), Set.of(
                        CharacterClass.WARRIOR
        )));

        register(new PlayerSkill("Strike", 9, 5,
                Attribute.STRENGTH, 1, TargetType.SINGLE_TARGET,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL), Set.of(
                        CharacterClass.WARRIOR
        )));

         register(new PlayerSkill("Raise Shield", 0, 5,
                Attribute.STRENGTH, 1, TargetType.SELF,
                Set.of(SkillTag.MELEE, SkillTag.PHYSICAL), Set.of(
                        CharacterClass.WARRIOR
         )));

        // MAGE SKILLS
        register(new PlayerSkill("Basic Magic Attack", 4, 0,
                Attribute.STRENGTH, 1, TargetType.SINGLE_TARGET,
                Set.of(SkillTag.RANGED, SkillTag.FIRE), Set.of(
                        CharacterClass.MAGICIAN
        )));

    }


    public static void register(PlayerSkill skill) {
        SKILLS.put(skill.getName().toLowerCase(), skill);
    }

    public static PlayerSkill get(String name) {
        return SKILLS.get(name.toLowerCase());
    }

    public static Map<String, PlayerSkill> getAll() {
        return Collections.unmodifiableMap(SKILLS);
    }

}
