package dao;

import model.User;
import database.DatabaseConfig;
import java.sql.*;

public class UserDAO {
    /**
     * Create a new user
     */
    public boolean createUser(model.User user) {
       String sql = "INSERT INTO users (username, password_hash, full_name, email, phone_number, aadhaar_number, is_admin, is_frozen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
       try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
           java.sql.PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
          stmt.setString(1, user.getUsername());
          stmt.setString(2, user.getPasswordHash());
          stmt.setString(3, user.getFullName());
          stmt.setString(4, user.getEmail());
          stmt.setString(5, user.getPhoneNumber());
          stmt.setString(6, user.getAadhaarNumber());
          stmt.setBoolean(7, user.isAdmin());
          stmt.setBoolean(8, user.isFrozen());
          int rows = stmt.executeUpdate();
            if (rows > 0) {
                java.sql.ResultSet rs = stmt.getGeneratedKeys();
                int userId = 0;
                if (rs.next()) {
                    userId = rs.getInt(1);
                } else {
                    // fallback: fetch user by username
                    model.User createdUser = getUserByUsername(user.getUsername());
                    if (createdUser != null) userId = createdUser.getId();
                }
                // Generate unique account number (e.g., timestamp + userId)
                String accountNumber = "ACCT" + System.currentTimeMillis() + userId;
                model.Account account = new model.Account(0, userId, java.math.BigDecimal.ZERO, false, accountNumber);
                new dao.AccountDAO().createAccount(account);
                return true;
            }
            return false;
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
                    rs.getString("phone_number"),
                    rs.getString("aadhaar_number"),
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
        String sql = "UPDATE users SET username = ?, password_hash = ?, full_name = ?, email = ?, phone_number = ?, aadhaar_number = ?, is_admin = ?, is_frozen = ? WHERE id = ?";
        try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAadhaarNumber());
            stmt.setBoolean(7, user.isAdmin());
            stmt.setBoolean(8, user.isFrozen());
            stmt.setInt(9, user.getId());
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
}
