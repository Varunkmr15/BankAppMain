package model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;
    private boolean isFrozen;

    public Account() {}

    public Account(int accountId, int userId, BigDecimal balance, boolean isFrozen) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.isFrozen = isFrozen;
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
                '}';
    }
}
