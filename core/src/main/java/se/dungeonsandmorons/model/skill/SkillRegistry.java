package se.dungeonsandmorons.model.skill;

import java.util.*;

public class SkillRegistry {

    private static final Map<String, Skill> SKILLS_BY_ID = new HashMap<>();
    private static final Map<String, Skill> SKILLS_BY_NAME = new HashMap<>();

    static {
        WarriorSkills.registerSkills();
        ArcherSkills.registerSkills();
    }

    public static void register(Skill skill) {
        if (skill.getId() != null) {
            SKILLS_BY_ID.put(skill.getId().toLowerCase(), skill);
        }
        SKILLS_BY_NAME.put(skill.name().toLowerCase(), skill);
    }

    public static Skill get(String nameOrId) {
        if (nameOrId == null) return null;
        String key = nameOrId.toLowerCase();
        Skill skill = SKILLS_BY_NAME.get(key);

        return skill != null ? skill : SKILLS_BY_ID.get(key);
    }

    public static Map<String, Skill> getAll() {
        return Collections.unmodifiableMap(SKILLS_BY_ID);
    }



}
