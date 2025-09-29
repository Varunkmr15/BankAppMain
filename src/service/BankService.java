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
    public boolean deposit(int accountId, double amount) {
        Account account = accountDAO.getAccountByUserId(accountId);
        if (account == null || account.isFrozen()) return false;
        double newBalance = account.getBalance().doubleValue() + amount;
        boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance);
        if (updated) {
            Transaction tx = new model.Transaction(0, account.getAccountId(), java.math.BigDecimal.valueOf(amount), "DEPOSIT", java.time.LocalDateTime.now(), "Deposit");
            transactionDAO.addTransaction(tx);
        }
        return updated;
    }

    /**
     * Withdraw money from an account
     */
    public boolean withdraw(int accountId, double amount) {
        Account account = accountDAO.getAccountByUserId(accountId);
        if (account == null || account.isFrozen() || account.getBalance().doubleValue() < amount) return false;
        double newBalance = account.getBalance().doubleValue() - amount;
        boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance);
        if (updated) {
            Transaction tx = new model.Transaction(0, account.getAccountId(), java.math.BigDecimal.valueOf(-amount), "WITHDRAW", java.time.LocalDateTime.now(), "Withdraw");
            transactionDAO.addTransaction(tx);
        }
        return updated;
    }

    /**
     * Transfer money between accounts
     */
    public boolean transfer(int fromAccountId, int toAccountId, double amount) {
        Account fromAccount = accountDAO.getAccountByUserId(fromAccountId);
        Account toAccount = accountDAO.getAccountByUserId(toAccountId);
        if (fromAccount == null || toAccount == null || fromAccount.isFrozen() || toAccount.isFrozen() || fromAccount.getBalance().doubleValue() < amount) return false;
        boolean withdrawSuccess = withdraw(fromAccountId, amount);
        boolean depositSuccess = deposit(toAccountId, amount);
        if (withdrawSuccess && depositSuccess) {
            Transaction tx = new model.Transaction(0, fromAccount.getAccountId(), java.math.BigDecimal.valueOf(-amount), "TRANSFER", java.time.LocalDateTime.now(), "Transfer to account " + toAccountId);
            transactionDAO.addTransaction(tx);
            Transaction tx2 = new model.Transaction(0, toAccount.getAccountId(), java.math.BigDecimal.valueOf(amount), "TRANSFER", java.time.LocalDateTime.now(), "Transfer from account " + fromAccountId);
            transactionDAO.addTransaction(tx2);
            return true;
        }
        return false;
    }

    /**
     * Get account balance
     */
    public double getBalance(int accountId) {
        Account account = accountDAO.getAccountByUserId(accountId);
        return account != null ? account.getBalance().doubleValue() : 0.0;
    }

    /**
     * Get transaction history
     */
    public java.util.List<model.Transaction> getTransactionHistory(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
    // ...existing code...
}
