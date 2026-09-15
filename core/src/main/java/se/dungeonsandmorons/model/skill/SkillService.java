package se.dungeonsandmorons.model.skill;
import se.dungeonsandmorons.model.character.Kharacter;

import java.util.List;

public class SkillService {

    public void listSkills(Kharacter player) {
        List<Skill> skills = player.getSkills();
        if (skills == null || skills.isEmpty()) {
            IO.println(String.format("%s has not learned any skills.", player.getName()));
            return;
        }

        skills.forEach(skill ->
                IO.println(String.format(
                        "Skill{name=%s, baseDamage=%d, resourceCost=%d, attribute=%s, targetType=%s, tags=N/A, allowedClasses=N/A}",
                        skill.getName(),
                        skill.getBaseDamage(),
                        skill.getResourceCost(),
                        skill.getScalingAttribute(),
                        skill.getTargetType()))
        );
    }
}
