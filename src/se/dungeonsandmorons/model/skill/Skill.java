package se.dungeonsandmorons.model.skill;

import se.dungeonsandmorons.model.Attribute;

public class Skill {
    private final String name;
    private final String sound;
    private final int baseDamage;
    private final Attribute attribute;
    private final int attributeDamageMultiplier;
    private final int resourceCost;

    public Skill(String name, String sound, int baseDamage, Attribute attribute, int attributeDamageMultiplier, int resourceCost) {
        this.name = name;
        this.sound = sound;
        this.baseDamage = baseDamage;
        this.attribute = attribute;
        this.attributeDamageMultiplier = attributeDamageMultiplier;
        this.resourceCost = resourceCost;
    }


    public String getName() { return this.name; }
    public String getSound() { return this.sound; }
    public int getBaseDamage() { return this.baseDamage; }
    public Attribute getAttribute() { return this.attribute; }
    public int getAttributeDamageMultiplier() { return this.attributeDamageMultiplier; }
    public int getResourceCost() { return this.resourceCost; }


}
