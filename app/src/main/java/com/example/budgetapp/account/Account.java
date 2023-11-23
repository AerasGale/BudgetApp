package com.example.budgetapp.account;

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
    @ColumnInfo(name = "starting_balance")
    private BigDecimal startingBalance;
    @ColumnInfo(name = "account_balance")
    private BigDecimal accountBalance;
    @ColumnInfo(name = "icon_id")
    private int iconResId;

    public Account(@NonNull String accountName, BigDecimal startingBalance, int iconResId) {
        this.accountName = accountName;
        this.startingBalance = startingBalance;
        this.accountBalance = startingBalance;
        this.iconResId = iconResId;
    }

    @NonNull
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(@NonNull String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getStartingBalance(){
        return startingBalance;
    }
    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{" +
                "accountName='" + accountName + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
