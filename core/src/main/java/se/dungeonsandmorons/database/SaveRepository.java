package se.dungeonsandmorons.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;
import se.dungeonsandmorons.model.skill.Skill;
import se.dungeonsandmorons.model.skill.SkillRegistry;
import se.dungeonsandmorons.town.TownRegistry;

// TODO: Change SaveRepository to non-static
public class SaveRepository {
    //private static final String URL = "jdbc:postgresql://localhost:5432/dungeonsandmorons";
    private static final String URL = "jdbc:postgresql://ep-cool-art-b2km85jl-pooler.c-6.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_pTFZ0keKunD1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void save(GameContext context) {
        String playerSql = """
                INSERT INTO character_saves (name, character_class, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana, last_town_visited, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
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
                last_town_visited = EXCLUDED.last_town_visited,
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

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            int characterId;

             try (PreparedStatement charStmt = conn.prepareStatement(playerSql)) {

                 charStmt.setString(1, context.getPlayer().getName());
                 charStmt.setString(2, context.getPlayer().getCharacterClass().toString());
                 charStmt.setInt(3, context.getPlayer().getLevel());
                 charStmt.setInt(4, context.getPlayer().getExperience());
                 charStmt.setInt(5, context.getPlayer().getStrength());
                 charStmt.setInt(6, context.getPlayer().getAgility());
                 charStmt.setInt(7, context.getPlayer().getIntelligence());
                 charStmt.setInt(8, context.getPlayer().getVitality());
                 charStmt.setInt(9, context.getPlayer().getCurrentHealth());
                 charStmt.setInt(10, context.getPlayer().getMaxHealth());
                 charStmt.setInt(11, context.getPlayer().getCurrentMana());
                 charStmt.setInt(12, context.getPlayer().getMaxMana());

                 String townName = context.getActiveTown() != null ? context.getActiveTown().getName() : "Densewood";
                 charStmt.setString(13, townName);

                 try (ResultSet rs = charStmt.executeQuery()) {
                     if (rs.next()) {
                         characterId = rs.getInt("id");
                     } else {
                         conn.rollback();
                         return;
                     }
                 }
             }

             if (context.getPlayer().getClearedDungeons() != null && !context.getPlayer().getClearedDungeons().isEmpty()) {

                 try (PreparedStatement dungeonStmt = conn.prepareStatement(dungeonSql)) {
                     for (String dungeon : context.getPlayer().getClearedDungeons()) {
                         dungeonStmt.setInt(1, characterId);
                         dungeonStmt.setString(2, dungeon);
                         dungeonStmt.addBatch();
                     }
                     dungeonStmt.executeBatch();
                 }
             }

             if (context.getPlayer().getSkills() != null && !context.getPlayer().getSkills().isEmpty()) {

                 try (PreparedStatement skillStmt = conn.prepareStatement(skillSql)) {
                     for (Skill skill : context.getPlayer().getSkills()) {
                         skillStmt.setInt(1, characterId);
                         skillStmt.setString(2, skill.getName());
                         skillStmt.addBatch();
                     }
                     skillStmt.executeBatch();
                 }
             }

             conn.commit();
             IO.println(String.format("Successfully saved '%s'.", context.getPlayer().getName()));

        } catch (SQLException e) {
            System.err.println("Failed to save character: " + e.getMessage());

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Failed to close connection: " + closeEx.getMessage());
                }
            }
        }
    }

    public boolean load(GameContext context, String name) {
        String playerSql    = "SELECT id, character_class, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana, last_town_visited FROM character_saves WHERE name = ?";
        String dungeonSql   = "SELECT dungeon_name FROM character_dungeons_cleared WHERE character_id = ?";
        String skillSql     = "SELECT skill_name FROM character_skills_learned WHERE character_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement playerStmt = conn.prepareStatement(playerSql)) {

                playerStmt.setString(1, name);

                try (ResultSet rs = playerStmt.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }

                    int characterId             = rs.getInt("id");
                    String className            = rs.getString("character_class");
                    int level                   = rs.getInt("level");
                    int experience              = rs.getInt("experience");
                    int strength                = rs.getInt("strength");
                    int agility                 = rs.getInt("agility");
                    int intelligence            = rs.getInt("intelligence");
                    int vitality                = rs.getInt("vitality");
                    int health                  = rs.getInt("health");
                    int max_health              = rs.getInt("max_health");
                    int mana                    = rs.getInt("mana");
                    int max_mana                = rs.getInt("max_mana");
                    String last_town_visited    = rs.getString("last_town_visited");

                    Character player = switch (className.toLowerCase()) {
                        case "warrior" -> new Warrior(characterId, name, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana);
                        //case "Magician" -> new Magician(characterId, name, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana);

                        default -> throw new IllegalArgumentException("Unknown class: " + className);
                    };


                    List<String> clearedDungeons = new ArrayList<>();
                    try (PreparedStatement dungeonStmt = conn.prepareStatement(dungeonSql)) {
                        dungeonStmt.setInt(1, characterId);
                        try (ResultSet dungeonRs = dungeonStmt.executeQuery()) {
                            while (dungeonRs.next()) {
                                clearedDungeons.add(dungeonRs.getString("dungeon_name"));
                            }
                            player.setClearedDungeons(clearedDungeons);
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

                    context.setPlayer(player);
                    if (last_town_visited != null) {
                        context.setActiveTown(TownRegistry.get(last_town_visited));
                    } else {
                        context.setActiveTown(TownRegistry.get("Densewood"));
                    }

                    return true;
                }

        } catch (SQLException e) {
            System.err.println("Failed to load character: " + e.getMessage());
            return false;
        }
    }

    public List<CharacterSummary> fetchSaves() {
        List<CharacterSummary> saves = new ArrayList<>();

        String sql = "SELECT name, character_class, level FROM character_saves WHERE id >= 0";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                saves.add(new CharacterSummary(
                        rs.getString("name"),
                        rs.getString("character_class"),
                        rs.getInt("level")
                ));
            }

        } catch (SQLException e) {
                 System.err.println("Failed to fetch characters: " + e.getMessage());
        }

        return saves;
    }


}
