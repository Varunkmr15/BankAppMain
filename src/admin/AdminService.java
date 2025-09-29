package admin;

import dao.UserDAO;
import dao.AccountDAO;
import model.User;
import model.Account;

public class AdminService {
    private UserDAO userDAO = new UserDAO();
    private AccountDAO accountDAO = new AccountDAO();

    /**
     * View all accounts
     */
    public java.util.List<model.Account> viewAllAccounts() {
        java.util.List<model.Account> accounts = new java.util.ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.Account account = new model.Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("balance"),
                    rs.getBoolean("is_frozen")
                );
                accounts.add(account);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    /**
     * Search account by accountId
     */
    public model.Account searchAccount(int accountId) {
        return accountDAO.getAccountByUserId(accountId);
    }

    /**
     * Delete or freeze account
     */
    public boolean deleteOrFreezeAccount(int accountId, boolean freeze) {
        if (freeze) {
            return accountDAO.setAccountFrozen(accountId, true);
        } else {
            return accountDAO.deleteAccount(accountId);
        }
    }

    /**
     * Generate simple report: total accounts, total balance
     */
    public String generateReports() {
        String sql = "SELECT COUNT(*) AS total_accounts, SUM(balance) AS total_balance FROM accounts";
        try (java.sql.Connection conn = database.DatabaseConfig.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int totalAccounts = rs.getInt("total_accounts");
                java.math.BigDecimal totalBalance = rs.getBigDecimal("total_balance");
                return "Total Accounts: " + totalAccounts + ", Total Balance: " + totalBalance;
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return "Report generation failed.";
    }
}
