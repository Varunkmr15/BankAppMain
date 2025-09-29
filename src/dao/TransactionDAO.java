package dao;

import model.Transaction;
import database.DatabaseConfig;
import java.sql.*;
import java.util.List;

public class TransactionDAO {
    /**
     * Add a transaction record
     */
    public boolean addTransaction(Transaction tx) {
        String sql = "INSERT INTO transactions (account_id, amount, type, timestamp, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tx.getAccountId());
            stmt.setBigDecimal(2, tx.getAmount());
            stmt.setString(3, tx.getType());
            stmt.setObject(4, tx.getTimestamp());
            stmt.setString(5, tx.getDescription());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all transactions for an account
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new java.util.ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
        try (Connection conn = database.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaction tx = new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getInt("account_id"),
                    rs.getBigDecimal("amount"),
                    rs.getString("type"),
                    rs.getTimestamp("timestamp").toLocalDateTime(),
                    rs.getString("description")
                );
                transactions.add(tx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
