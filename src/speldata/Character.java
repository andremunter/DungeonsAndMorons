package speldata;

public abstract class Character {
    private String name;
    private int hp;

    private int level;
    private int xp;
    private int strength;
    private int agility;
    private int intelligence;

    Character(int strength, int agility, int intelligence){
        this.hp = 100;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
    }

    void levelUp(){}

    void die(){

    }

    public int getHp(){
        return this.hp;
    }

    public void setHp(int newHp){
        this.hp = newHp;
    }

    public int getStrength(){
        return strength;
    }

    public int getAgility(){
        return agility;
    }

    public int getIntelligence(){
        return intelligence;
    }
}
