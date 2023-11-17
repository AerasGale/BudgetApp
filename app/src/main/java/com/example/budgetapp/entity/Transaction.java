package com.example.budgetapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(
                entity = Account.class,
                parentColumns = "account_name",
                childColumns = "account_name",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
                ),
        indices = { @Index(value = {"account_name"})})
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trx_id")
    private long trxId;
    @ColumnInfo(name = "account_name")
    private String accountName;
    @ColumnInfo(name = "transaction_type")
    private TransactionType trxType;
    @ColumnInfo(name = "entry_time")
    private ZonedDateTime entryTime;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "amount")
    private BigDecimal amount;

    public Transaction(String accountName, TransactionType trxType, ZonedDateTime entryTime, String description, BigDecimal amount) {

        this.accountName = accountName;
        this.trxType = trxType;
        this.entryTime = entryTime;
        this.description = description;
        this.amount = amount;
    }

    public long getTrxId() {
        return trxId;
    }

    public void setTrxId(long trxId) {
        this.trxId = trxId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public TransactionType getTrxType() {
        return trxType;
    }

    public void setTrxType(TransactionType trxType) {
        this.trxType = trxType;
    }

    public ZonedDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(ZonedDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "trxId=" + trxId +
                ", accountName='" + accountName + '\'' +
                ", trxType=" + trxType +
                ", entryTime=" + entryTime +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
