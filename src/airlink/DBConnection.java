package airlink;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
    }

    private static final String URL = getEnv("AIRLINK_DB_URL", "jdbc:postgresql://localhost:5432/airlinkdb");
    private static final String USER = getEnv("AIRLINK_DB_USER", "postgres");
    private static final String PASSWORD = getEnv("AIRLINK_DB_PASSWORD", "Jungkook@999");

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("Database connection failed. Check AIRLINK_DB_URL / AIRLINK_DB_USER / AIRLINK_DB_PASSWORD.");
            e.printStackTrace();
        }

        return null;
    }
}