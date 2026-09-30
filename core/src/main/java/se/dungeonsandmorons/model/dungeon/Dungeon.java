package se.dungeonsandmorons.model.dungeon;

import se.dungeonsandmorons.model.monster.MonsterRarity;
import se.dungeonsandmorons.model.monster.Monster;
import se.dungeonsandmorons.model.monster.MonsterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dungeon {
    private final String id;
    private final String name;
    private final String description;
    private final String townId;
    private final int areaLevel;
    private boolean cleared;

    private final List<Monster> monsters = new ArrayList<>();

    public Dungeon(
            String id,
            String name,
            String description,
            String townId,
            int areaLevel,
            List<MonsterFactory> monsterRecipes
    ) {
        this.id = Objects.requireNonNull(id, "Dungeon id cannot be null");
        this.name = Objects.requireNonNull(name, "Dungeon name cannot be null");
        this.description = Objects.requireNonNull(description, "Dungeon description cannot be null");
        this.townId = Objects.requireNonNull(townId, "Dungeon townId cannot be null");
        if (areaLevel < 1) {
            throw new IllegalArgumentException("Dungeon Area Level must be at least 1, received: " + areaLevel);
        }
        this.areaLevel = areaLevel;
        this.cleared = false;

        if (monsterRecipes != null && !monsterRecipes.isEmpty()) {
            populateMonsters(monsterRecipes);
        }
    }

    public void populateMonsters(List<MonsterFactory> recipes) {
        Objects.requireNonNull(recipes, "Recipe list cannot be null");
        for (MonsterFactory recipe : recipes) {
            this.monsters.add(recipe.create(this.areaLevel, MonsterRarity.NORMAL));
        }

    }

    public void spawnBoss(MonsterFactory bossRecipe) {
        Objects.requireNonNull(bossRecipe, "Boss recipe cannot be null");
        this.monsters.add(bossRecipe.create(this.areaLevel, MonsterRarity.BOSS));
    }

    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTownId() { return townId; }
    public int getAreaLevel() { return areaLevel; }
    public boolean isCleared() { return cleared; }
    public List<Monster> getMonsters() { return List.copyOf(monsters); }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String description = "";
        private String townId;
        private int areaLevel;
        private List<MonsterFactory> monsterRecipes = new ArrayList<>();

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder townId(String townId) { this.townId = townId; return this; }
        public Builder areaLevel(int areaLevel) { this.areaLevel = areaLevel; return this; }
        public Builder monsterRecipes(List<MonsterFactory> recipes) { this.monsterRecipes = recipes; return this; }

        public Dungeon build() {
            return new Dungeon(id, name, description, townId, areaLevel, monsterRecipes);
        }
    }

    @Override
    public String toString() {
        return String.format("%s, '%s', Area Level: %d%s" , name, description, areaLevel, cleared ? " (Cleared)" : "");
    }

}
