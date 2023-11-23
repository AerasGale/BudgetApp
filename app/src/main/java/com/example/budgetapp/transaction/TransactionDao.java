package com.example.budgetapp.transaction;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetapp.transaction.Transaction;
import com.example.budgetapp.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    public void insertOne(Transaction trx);
    @Update
    public void updateOne(Transaction trx);
    @Delete
    public void deleteOne(Transaction trx);

    @Query("SELECT * FROM transactions")
    public LiveData<List<Transaction>> getAllTransactions();
    @Query("SELECT * FROM transactions WHERE account_name = :account_name ")
    public LiveData<List<Transaction>> getTransactionsFromAccount(String account_name);
    @Query("SELECT * FROM transactions WHERE transaction_type = :type")
    public LiveData<List<Transaction>> getTransactionsOfType(TransactionType type);
    @Query("SELECT SUM(amount) FROM transactions WHERE transaction_type = :type AND account_name = :name")
    public BigDecimal getSumOfTypeFrom(TransactionType type, String name);
}
