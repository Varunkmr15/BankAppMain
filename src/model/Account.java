package model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;
    private boolean isFrozen;
    private String accountNumber;

    public Account() {}

    public Account(int accountId, int userId, BigDecimal balance, boolean isFrozen, String accountNumber) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.isFrozen = isFrozen;
        this.accountNumber = accountNumber;
    }

    public Account(int accountId, int userId, BigDecimal balance, boolean isFrozen) {
        this(accountId, userId, balance, isFrozen, null);
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                ", isFrozen=" + isFrozen +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
