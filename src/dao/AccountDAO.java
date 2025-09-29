package dao;

import model.Account;
import database.DatabaseConfig;
import java.sql.*;

public class AccountDAO {
    /**
     * Get account by account ID
     */
    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account = new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("balance"),
                    rs.getBoolean("is_frozen"),
                    rs.getString("account_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    /**
     * Get account by user ID
     */
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account = new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("balance"),
                    rs.getBoolean("is_frozen"),
                    rs.getString("account_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * Update account balance
     */
    public boolean updateBalance(int accountId, double amount) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, java.math.BigDecimal.valueOf(amount));
            stmt.setInt(2, accountId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create a new account
     */
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (user_id, balance, is_frozen, account_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, account.getUserId());
            stmt.setBigDecimal(2, account.getBalance());
            stmt.setBoolean(3, account.isFrozen());
            stmt.setString(4, account.getAccountNumber());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete an account
     */
    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Freeze or unfreeze an account
     */
    public boolean setAccountFrozen(int accountId, boolean frozen) {
        String sql = "UPDATE accounts SET is_frozen = ? WHERE account_id = ?";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, frozen);
            stmt.setInt(2, accountId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // ...existing code...
}
