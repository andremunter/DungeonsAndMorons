package se.dungeonsandmorons.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //private static final String URL = "jdbc:postgresql://localhost:5432/dungeonsandmorons";
    private static final String URL = "jdbc:postgresql://ep-cool-art-b2km85jl-pooler.c-6.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_pTFZ0keKunD1";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
