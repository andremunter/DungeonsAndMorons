package se.dungeonsandmorons.model.npc;

import se.dungeonsandmorons.model.combat.Targetable;
import se.dungeonsandmorons.model.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public abstract class Npc implements Targetable {
    protected final String name;
    private int maxHealth;
    protected int currentHealth;
    protected int expOnDeath;

    protected List<Skill> skills = new ArrayList<>();

    Npc(String name, int maxHealth, int expOnDeath) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.expOnDeath = expOnDeath;
    }


    public boolean isAlive() { return this.currentHealth > 0; }

    public String getName() { return this.name;}
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public int getCurrentHealth() { return this.currentHealth; }
    public List<Skill> getSkills() { return this.skills; }

    public void addExperience(int experience) {

    }


}
