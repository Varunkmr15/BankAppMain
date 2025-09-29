package service;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.Transaction;

public class BankService {
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    /**
     * Deposit money into an account
     */
    /**
     * Deposit money into an account by accountId
     */
    public boolean deposit(int accountId, double amount) {
        Account account = getAccountByAccountId(accountId);
        if (account == null || account.isFrozen()) return false;
        double newBalance = account.getBalance().doubleValue() + amount;
        boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance);
        if (updated) {
            Transaction tx = new Transaction(0, account.getAccountId(), java.math.BigDecimal.valueOf(amount), "DEPOSIT", java.time.LocalDateTime.now(), "Deposit");
            transactionDAO.addTransaction(tx);
        }
        return updated;
    }

    /**
     * Withdraw money from an account
     */
    /**
     * Withdraw money from an account by accountId
     */
    public boolean withdraw(int accountId, double amount) {
        Account account = getAccountByAccountId(accountId);
        if (account == null || account.isFrozen() || account.getBalance().doubleValue() < amount) return false;
        double newBalance = account.getBalance().doubleValue() - amount;
        boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance);
        if (updated) {
            Transaction tx = new Transaction(0, account.getAccountId(), java.math.BigDecimal.valueOf(-amount), "WITHDRAW", java.time.LocalDateTime.now(), "Withdraw");
            transactionDAO.addTransaction(tx);
        }
        return updated;
    }

    /**
     * Transfer money between accounts
     */
    /**
     * Transfer money between accounts by accountId
     */
    public boolean transfer(int fromAccountId, int toAccountId, double amount) {
        Account fromAccount = getAccountByAccountId(fromAccountId);
        Account toAccount = getAccountByAccountId(toAccountId);
        if (fromAccount == null || toAccount == null || fromAccount.isFrozen() || toAccount.isFrozen() || fromAccount.getBalance().doubleValue() < amount) return false;
        boolean withdrawSuccess = withdraw(fromAccountId, amount);
        boolean depositSuccess = deposit(toAccountId, amount);
        if (withdrawSuccess && depositSuccess) {
            Transaction tx = new Transaction(0, fromAccount.getAccountId(), java.math.BigDecimal.valueOf(-amount), "TRANSFER", java.time.LocalDateTime.now(), "Transfer to account " + toAccountId);
            transactionDAO.addTransaction(tx);
            Transaction tx2 = new Transaction(0, toAccount.getAccountId(), java.math.BigDecimal.valueOf(amount), "TRANSFER", java.time.LocalDateTime.now(), "Transfer from account " + fromAccountId);
            transactionDAO.addTransaction(tx2);
            return true;
        }
        return false;
    }

    /**
     * Get account balance
     */
    /**
     * Get account balance by accountId
     */
    public double getBalance(int accountId) {
        Account account = getAccountByAccountId(accountId);
        return account != null ? account.getBalance().doubleValue() : 0.0;
    }
    /**
     * Helper: Get account by accountId
     */
    private Account getAccountByAccountId(int accountId) {
    return accountDAO.getAccountByAccountId(accountId);
    }

    /**
     * Get transaction history
     */
    public java.util.List<model.Transaction> getTransactionHistory(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
    // ...existing code...
}
