package com.example.budgetapp.account;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetapp.account.Account;

import java.math.BigDecimal;
import java.util.List;

@Dao
public interface AccountDao {
    @Insert
    public void insertOne(Account account);
    @Update
    public void updateOne(Account account);
    @Delete
    public void deleteOne(Account account);

    @Query("SELECT * FROM accounts")
    public LiveData<List<Account>> getAllAccounts();
    @Query("SELECT account_name FROM accounts")
    public LiveData<List<String>> getAccountNames();
    @Query("SELECT * FROM accounts WHERE account_name = :name")
    public Account getAccountByName(String name);
    @Query("SELECT SUM(account_balance) FROM accounts")
    public LiveData<BigDecimal> getSumOfAccountBalance();

}
