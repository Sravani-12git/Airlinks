package airlink;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/airlinkdb";

    private static final String USER = "postgres";

    private static final String PASSWORD = "Jungkook@999";

    public static Connection getConnection() {

        try {

            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}