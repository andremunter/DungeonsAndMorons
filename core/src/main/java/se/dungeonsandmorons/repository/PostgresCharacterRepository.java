package se.dungeonsandmorons.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import se.dungeonsandmorons.exception.CorruptedSaveException;
import se.dungeonsandmorons.game.GameContext;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.model.skill.Skill;
import se.dungeonsandmorons.model.skill.SkillRegistry;
import se.dungeonsandmorons.town.TownRegistry;

public class PostgresCharacterRepository implements CharacterRepository {
    //private static final String URL = "jdbc:postgresql://localhost:5432/dungeonsandmorons";
    private static final String URL = "jdbc:postgresql://ep-cool-art-b2km85jl-pooler.c-6.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_pTFZ0keKunD1";

    private static final String SELECT_PLAYER_SQL = """
            SELECT id, character_class, level, experience, strength, agility, intelligence,
            vitality, health, max_health, mana, max_mana, last_town_visited, unallocated_stat_points
            FROM character_saves
            WHERE name = ?;
            """;

    private static final String SELECT_DUNGEONS_SQL = """
            SELECT dungeon_name
            FROM character_dungeons_cleared
            WHERE character_id = ?
            """;

    private static final String SELECT_SKILLS_SQL = """
            SELECT skill_name
            FROM character_skills_learned
            WHERE character_id = ?;
            """;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public void save(Kharacter player, String lastTownVisited) {
        String playerSql = """
                INSERT INTO character_saves (name, character_class, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana, last_town_visited, unallocated_stat_points, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
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
                unallocated_stat_points = EXCLUDED.unallocated_stat_points,
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

                 charStmt.setString(1,  player.getName());
                 charStmt.setString(2,  player.getCharacterClass().toString());
                 charStmt.setInt(3,     player.getLevel());
                 charStmt.setInt(4,     player.getExperience());
                 charStmt.setInt(5,     player.getStrength());
                 charStmt.setInt(6,     player.getAgility());
                 charStmt.setInt(7,     player.getIntelligence());
                 charStmt.setInt(8,     player.getVitality());
                 charStmt.setInt(9,     player.getCurrentHealth());
                 charStmt.setInt(10,    player.getMaxHealth());
                 charStmt.setInt(11,    player.getCurrentMana());
                 charStmt.setInt(12,    player.getMaxMana());
                 charStmt.setInt(14,    player.getUnallocatedStatPoints());

                 charStmt.setString(13, lastTownVisited);

                 try (ResultSet rs = charStmt.executeQuery()) {
                     if (rs.next()) {
                         characterId = rs.getInt("id");
                     } else {
                         conn.rollback();
                         return;
                     }
                 }
             }

             if (player.getClearedDungeons() != null && !player.getClearedDungeons().isEmpty()) {

                 try (PreparedStatement dungeonStmt = conn.prepareStatement(dungeonSql)) {
                     for (String dungeon : player.getClearedDungeons()) {
                         dungeonStmt.setInt(1, characterId);
                         dungeonStmt.setString(2, dungeon);
                         dungeonStmt.addBatch();
                     }
                     dungeonStmt.executeBatch();
                 }
             }

             if (player.getSkills() != null && !player.getSkills().isEmpty()) {

                 try (PreparedStatement skillStmt = conn.prepareStatement(skillSql)) {
                     for (Skill skill : player.getSkills()) {
                         skillStmt.setInt(1, characterId);
                         skillStmt.setString(2, skill.getName());
                         skillStmt.addBatch();
                     }
                     skillStmt.executeBatch();
                 }
             }

             conn.commit();
             IO.println(String.format("Successfully saved '%s'.", player.getName()));

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

    @Override
    public Optional<CharacterSaveData> findByName(String name) {

        try (Connection conn = getConnection();
                PreparedStatement playerStmt = conn.prepareStatement(SELECT_PLAYER_SQL)) {

                playerStmt.setString(1, name);

                try (ResultSet rs = playerStmt.executeQuery()) {
                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    int characterId             = rs.getInt(    "id");
                    int level                   = rs.getInt(    "level");
                    String className            = rs.getString( "character_class");
                    int experience              = rs.getInt(    "experience");
                    int strength                = rs.getInt(    "strength");
                    int agility                 = rs.getInt(    "agility");
                    int intelligence            = rs.getInt(    "intelligence");
                    int vitality                = rs.getInt(    "vitality");
                    int health                  = rs.getInt(    "health");
                    int maxHealth               = rs.getInt(    "max_health");
                    int mana                    = rs.getInt(    "mana");
                    int maxMana                 = rs.getInt(    "max_mana");
                    int unallocatedStatPoints = rs.getInt(    "unallocated_stat_points");
                    String lastTownVisited    = rs.getString( "last_town_visited");

                    CharacterClass characterClass = parseCharacterClass(name, className);

                    Kharacter player = new Kharacter(
                            characterId, name, characterClass, level, experience,
                            unallocatedStatPoints, strength, agility, intelligence,
                            vitality, health, maxHealth, mana, maxMana
                    );

                    player.setClearedDungeons(loadClearedDungeons(conn, characterId, player));
                    loadLearnedSkills(conn, characterId, player);

                    return Optional.of(new CharacterSaveData(player, lastTownVisited));
                }

        } catch (SQLException e) {
            System.err.println("Failed to load character: " + e.getMessage());
            return Optional.empty();
        }
    }




    private static void loadLearnedSkills(Connection conn, int characterId, Kharacter player) throws SQLException {
        try (PreparedStatement skillStmt = conn.prepareStatement(SELECT_SKILLS_SQL)) {
            skillStmt.setInt(1, characterId);
            try (ResultSet skillRs = skillStmt.executeQuery()) {
                while (skillRs.next()) {
                    player.learnSkill(SkillRegistry.get(skillRs.getString("skill_name")));
                }
            }
        }
    }

    private List<String> loadClearedDungeons(Connection conn, int characterId, Kharacter player) throws SQLException {
        List<String> clearedDungeons = new ArrayList<>();
        try (PreparedStatement dungeonStmt = conn.prepareStatement(SELECT_DUNGEONS_SQL)) {
            dungeonStmt.setInt(1, characterId);
            try (ResultSet dungeonRs = dungeonStmt.executeQuery()) {
                while (dungeonRs.next()) {
                    clearedDungeons.add(dungeonRs.getString("dungeon_name"));
                }
            }
        }
        return clearedDungeons;
    }

    private CharacterClass parseCharacterClass(String name, String className) {
        if (className == null) {
            throw new IllegalArgumentException("Class name in database is null");
        }

        try {
            return CharacterClass.valueOf(className.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CorruptedSaveException(
                    String.format("Failed to load character '%s': Unknown or invalid class '%s'",
                            name, className), e
            );
        }
    }

    public List<CharacterSummary> findAllSummaries() {
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
