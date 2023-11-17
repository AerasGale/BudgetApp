package com.example.budgetapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(tableName = "accounts")
public class Account {
    @PrimaryKey
    @ColumnInfo(name = "account_name")
    @NonNull private String accountName;
    @ColumnInfo(name = "account_balance")
    private BigDecimal accountBalance;
    @ColumnInfo(name = "icon_id")
    private int iconResId;
    @ColumnInfo(name = "active_account")
    private boolean isActive;

    public Account(String accountName, BigDecimal accountBalance, int iconResId, boolean isActive) {
        this.accountName = accountName;
        this.accountBalance = accountBalance;
        this.iconResId = iconResId;
        this.isActive = isActive;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountName='" + accountName + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
