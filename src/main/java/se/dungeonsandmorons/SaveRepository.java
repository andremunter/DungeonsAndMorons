package se.dungeonsandmorons;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import se.dungeonsandmorons.model.character.Character;
import se.dungeonsandmorons.model.character.Warrior;

public class SaveRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/dungeonsandmorons";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void save(Character character) {
        String sql = """
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
                updated_at = CURRENT_TIMESTAMP;
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, character.getName());
            stmt.setString(2, character.getClass().getSimpleName());
            stmt.setInt(3, character.getLevel());
            stmt.setInt(4, character.getExperience());
            stmt.setInt(5, character.getStrength());
            stmt.setInt(6, character.getAgility());
            stmt.setInt(7, character.getIntelligence());
            stmt.setInt(8, character.getVitality());
            stmt.setInt(9, character.getCurrentHealth());
            stmt.setInt(10, character.getMaxHealth());
            stmt.setInt(11, character.getCurrentMana());
            stmt.setInt(12, character.getMaxMana());

            stmt.executeUpdate();
            IO.println(String.format("Saved %s to database.", character.getName()));

        } catch (SQLException e) {
            System.err.println("Failed to save character: " + e.getMessage());
        }
    }

    public static Character load(String name) {
        String sql = "SELECT character_class, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana FROM character_saves WHERE name = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, name);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
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

                        Character loadedCharacter = switch (className) {
                            case "Warrior" -> new Warrior(name, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana);
                            //case "Magician" -> new Magician(name, level, experience, strength, agility, intelligence, vitality, health, max_health, mana, max_mana);

                            default -> throw new IllegalArgumentException("Unknown class: " + className);
                        };

                        return loadedCharacter;
                    }
                }

        } catch (SQLException e) {
            System.err.println("Failed to load character: " + e.getMessage());
        }

        return null;
    }

    public static List<Character> fetchSaves() {
        List<Character> characters = new ArrayList<>();
        String sql = "SELECT name, character_class, level FROM character_saves WHERE id >= 0";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name             = rs.getString("name");
                String characterClazz   = rs.getString("character_class");
                int level               = rs.getInt("level");

                switch (characterClazz) {
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
