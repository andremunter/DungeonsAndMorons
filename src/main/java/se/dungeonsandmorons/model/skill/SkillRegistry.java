package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.Attribute;

import java.util.*;

public class SkillRegistry {

    private static final Map<String, Skill> SKILLS = new HashMap<>();


    static {
        register(new Skill("Strike", "", 5, Attribute.STRENGTH, 1, 5));
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

    public static Skill findSkillByName(String name) {
        return SKILLS.get(name);
    }

}
