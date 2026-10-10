package se.dungeonsandmorons.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import se.dungeonsandmorons.database.DatabaseConnection;
import se.dungeonsandmorons.exception.CorruptedSaveException;
import se.dungeonsandmorons.model.character.Kharacter;
import se.dungeonsandmorons.model.character.CharacterClass;
import se.dungeonsandmorons.model.skill.SkillRegistry;

import javax.sql.DataSource;

public class PostgresCharacterRepository implements CharacterRepository {

    private final DataSource dataSource;

    private static final String INSERT_PLAYER_SQL = """
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

    private static final String INSERT_DUNGEON_SQL = """
            INSERT INTO character_dungeons_cleared (character_id, dungeon_name)
            VALUES (?, ?)
            ON CONFLICT (character_id, dungeon_name) DO NOTHING;
            """;

    private static final String INSERT_SKILL_SQL = """
            INSERT INTO character_skills_learned (character_id, skill_name)
            VALUES (?, ?)
            ON CONFLICT (character_id, skill_name) DO NOTHING;
            """;

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

    private static final String DELETE_PLAYER_SQL = """
            DELETE FROM character_saves
            WHERE name = ?;
            """;

    private static final String EXISTS_PLAYER_SQL = """
            SELECT 1
            FROM character_saves
            WHERE name = ?
            LIMIT 1;
            """;

    private static final String SELECT_ALL_SUMMARIES_SQL = """
        SELECT name, character_class, level, last_town_visited,
               health, max_health, mana, max_mana,
               strength, agility, intelligence, vitality
        FROM character_saves
        ORDER BY name;
        """;

    public PostgresCharacterRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PostgresCharacterRepository() {
        this.dataSource = null;
    }

    private Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        return DatabaseConnection.getConnection();
    }

    @Override
    public void save(Kharacter player, String lastTownVisited) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                int characterId = saveCharacterData(conn, player, lastTownVisited);
                saveClearedDungeons(conn, characterId, player.getClearedDungeons());
                saveLearnedSkills(conn, characterId, player.getSkillNames());
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.printf("Failed to save character '%s': %s%n", player.getName(), e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Database connection failure: " + e.getMessage());
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

    @Override
    public List<CharacterSummary> findAllSummaries() {
        List<CharacterSummary> saves = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SUMMARIES_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                saves.add(new CharacterSummary(
                        rs.getString("name"),
                        rs.getString("character_class"),
                        rs.getInt("level"),
                        rs.getString("last_town_visited"),
                        rs.getInt("health"),
                        rs.getInt("max_health"),
                        rs.getInt("mana"),
                        rs.getInt("max_mana"),
                        0, // Default 0 for armor; Armor is calculated when character/gear is loaded (Future WIP)
                        rs.getInt("strength"),
                        rs.getInt("agility"),
                        rs.getInt("intelligence"),
                        rs.getInt("vitality")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch characters: " + e.getMessage());
        }

        return saves;
    }

    @Override
    public boolean deleteByName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PLAYER_SQL)) {

            stmt.setString(1, name);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.printf("Could not delete character '%s': %s%n", name, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_PLAYER_SQL)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.printf("Database error when searching for '%s': %s%n", name, e.getMessage());
            return false;
        }
    }


    // ========================================================== //
    //                    PRIVATE HELP METHODS                    //
    // ========================================================== //

    private int saveCharacterData(Connection conn, Kharacter player, String lastTownVisited) throws SQLException {
        try (PreparedStatement charStmt = conn.prepareStatement(INSERT_PLAYER_SQL)) {

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
            charStmt.setString(13, lastTownVisited);
            charStmt.setInt(14,    player.getUnallocatedStatPoints());

            try (ResultSet rs = charStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                throw new SQLException("Creating character save failed, no ID obtained.");
            }
        }
    }

    private void saveClearedDungeons(Connection conn, int characterId, List<String> clearedDungeons) throws SQLException {
        if (clearedDungeons == null || clearedDungeons.isEmpty()) {
            return;
        }

        try (PreparedStatement dungStmt = conn.prepareStatement(INSERT_DUNGEON_SQL)) {
            for (String dungeon : clearedDungeons) {
                dungStmt.setInt(1, characterId);
                dungStmt.setString(2, dungeon);
                dungStmt.addBatch();
            }
            dungStmt.executeBatch();
        }
    }

    private void saveLearnedSkills(Connection conn, int characterId, List<String> skills) throws SQLException {
        if (skills == null || skills.isEmpty()) {
            return;
        }

        try (PreparedStatement skillStmt = conn.prepareStatement(INSERT_SKILL_SQL)) {
            for (String skill : skills) {
                skillStmt.setInt(1, characterId);
                skillStmt.setString(2, skill);
                skillStmt.addBatch();
            }
            skillStmt.executeBatch();
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

}
