package se.dungeonsandmorons;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.dungeon.Dungeon;
import se.dungeonsandmorons.model.skill.Skill;
import se.dungeonsandmorons.model.skill.SkillRegistry;

// TODO: Change SaveRepository to non-static
public class SaveRepository {
    //private static final String URL = "jdbc:postgresql://localhost:5432/dungeonsandmorons";
    private static final String URL = "jdbc:postgresql://ep-cool-art-b2km85jl-pooler.c-6.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_pTFZ0keKunD1";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void save(Character character) {
        String playerSql = """
                INSERT INTO character_saves (name, character_class, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                ON CONFLICT (name)
                DO UPDATE SET
                level = EXCLUDED.level,
                experience = EXCLUDED.experience,
                strength = EXCLUDED.strength,
                agility = EXCLUDED.agility,
                intelligence = EXCLUDED.intelligence,
                vitality = EXCLUDED.vitality,
                health = EXCLUDED.health,
                max_health = EXCLUDED.max_health,
                mana = EXCLUDED.mana,
                max_mana = EXCLUDED.max_mana,
                updated_at = CURRENT_TIMESTAMP
                RETURNING id;
                """;

        String dungeonSql = """
                INSERT INTO character_dungeons_cleared (character_id, dungeon_name)
                VALUES (?, ?)
                ON CONFLICT (character_id, dungeon_name) DO NOTHING;
                """;

        String skillSql = """
                INSERT INTO character_skills_learned (character_id, skill_name)
                VALUES (?, ?)
                ON CONFLICT (character_id, skill_name) DO NOTHING;
                """;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            int characterId;

             try (PreparedStatement charStmt = conn.prepareStatement(playerSql)) {

                charStmt.setString(1, character.getName());
                charStmt.setString(2, character.getCharacterClass().toString());
                charStmt.setInt(3, character.getLevel());
                charStmt.setInt(4, character.getExperience());
                charStmt.setInt(5, character.getStrength());
                charStmt.setInt(6, character.getAgility());
                charStmt.setInt(7, character.getIntelligence());
                charStmt.setInt(8, character.getVitality());
                charStmt.setInt(9, character.getCurrentHealth());
                charStmt.setInt(10, character.getMaxHealth());
                charStmt.setInt(11, character.getCurrentMana());
                charStmt.setInt(12, character.getMaxMana());

                try (ResultSet rs = charStmt.executeQuery()) {
                    if (rs.next()) {
                        characterId = rs.getInt("id");
                    } else {
                        conn.rollback();
                        return;
                    }
                }

                try (PreparedStatement dungeonStmt = conn.prepareStatement(dungeonSql)) {
                    for (Dungeon dungeon : character.getClearedDungeons()) {
                        dungeonStmt.setInt(1, characterId);
                        dungeonStmt.setString(2, dungeon.getName());
                        dungeonStmt.addBatch();
                    }
                    dungeonStmt.executeBatch();
                }

                try (PreparedStatement skillStmt = conn.prepareStatement(skillSql)) {
                    for (Skill skill : character.getSkills()) {
                        skillStmt.setInt(1, characterId);
                        skillStmt.setString(2, skill.getName());
                        skillStmt.addBatch();
                    }
                    skillStmt.executeBatch();
                }

                conn.commit();
                IO.println(String.format("Saved %s and cleared dungeons to database.", character.getName()));
            }
        } catch (SQLException e) {
            System.err.println("Failed to save character: " + e.getMessage());
        }
    }

    public static Character load(String name) {
        String playerSql    = "SELECT id, character_class, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana FROM character_saves WHERE name = ?";
        String dungeonSql   = "SELECT dungeon_name FROM character_dungeons_cleared WHERE character_id = ?";
        String skillSql     = "SELECT skill_name FROM character_skills_learned WHERE character_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement playerStmt = conn.prepareStatement(playerSql)) {

                playerStmt.setString(1, name);

                try (ResultSet rs = playerStmt.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    int characterId     = rs.getInt("id");
                    String className    = rs.getString("character_class");
                    int level           = rs.getInt("level");
                    int experience      = rs.getInt("experience");
                    int strength        = rs.getInt("strength");
                    int agility         = rs.getInt("agility");
                    int intelligence    = rs.getInt("intelligence");
                    int vitality        = rs.getInt("vitality");
                    int health          = rs.getInt("health");
                    int max_health      = rs.getInt("max_health");
                    int mana            = rs.getInt("mana");
                    int max_mana        = rs.getInt("max_mana");

                    Character player = switch (className.toLowerCase()) {
                        case "warrior" -> new Warrior(characterId, name, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana);
                        //case "Magician" -> new Magician(characterId, name, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana);

                        default -> throw new IllegalArgumentException("Unknown class: " + className);
                    };

                    try (PreparedStatement dungeonStmt = conn.prepareStatement(dungeonSql)) {
                        dungeonStmt.setInt(1, characterId);
                        try (ResultSet dungeonRs = dungeonStmt.executeQuery()) {
                            while (dungeonRs.next()) {
                                player.clearDungeon(dungeonRs.getString("dungeon_name"));
                            }
                        }
                    }

                    try (PreparedStatement skillStmt = conn.prepareStatement(skillSql)) {
                        skillStmt.setInt(1, characterId);
                        try (ResultSet skillRs = skillStmt.executeQuery()) {
                            while (skillRs.next()) {
                                player.learnSkill(SkillRegistry.get(skillRs.getString("skill_name")));
                            }
                        }
                    }

                    return player;
                }

        } catch (SQLException e) {
            System.err.println("Failed to load character: " + e.getMessage());
            return null;
        }
    }

    public static List<Character> fetchSaves() {
        List<Character> characters = new ArrayList<>();
        String sql = "SELECT name, character_class, level FROM character_saves WHERE id >= 0";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name             = rs.getString("name");
                String characterClass   = rs.getString("character_class");
                int level               = rs.getInt("level");

                switch (characterClass) {
                    case "Warrior"      -> characters.add(new Warrior(name, level));
                   // case "Magician"     -> characters.add(new Magician());
                   // case "Archer"       -> characters.add(new Archer());

                }

            }

        } catch (SQLException e) {
                 System.err.println("Failed to fetch characters: " + e.getMessage());
        }

        return characters;
    }


}
