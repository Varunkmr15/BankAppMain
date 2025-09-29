package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // JDBC URL, username and password of PostgreSQL server
    private static final String URL = "jdbc:postgresql://db.akogpjqcuukusvriulsq.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "anniop@1977";

    /**
     * Get a new database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Close a database connection safely
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
