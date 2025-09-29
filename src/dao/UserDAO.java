package dao;

import model.User;
import database.DatabaseConfig;
import java.sql.*;

public class UserDAO {
    /**
     * Create a new user
     */
    public boolean createUser(model.User user) {
        String sql = "INSERT INTO users (username, password_hash, full_name, email, is_admin, is_frozen) VALUES (?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isAdmin());
            stmt.setBoolean(6, user.isFrozen());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get user by username
     */
    public model.User getUserByUsername(String username) {
        model.User user = null;
        String sql = "SELECT * FROM users WHERE username = ?";
        try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new model.User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getBoolean("is_admin"),
                    rs.getBoolean("is_frozen")
                );
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Update user profile
     */
    public boolean updateUser(model.User user) {
        String sql = "UPDATE users SET full_name = ?, email = ?, is_admin = ?, is_frozen = ? WHERE username = ?";
        try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setBoolean(3, user.isAdmin());
            stmt.setBoolean(4, user.isFrozen());
            stmt.setString(5, user.getUsername());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete user by username
     */
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // ...existing code...
}
