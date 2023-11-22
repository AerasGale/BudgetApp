package com.example.budgetapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Query;

import com.example.budgetapp.dao.AccountDao;
import com.example.budgetapp.dao.TransactionDao;
import com.example.budgetapp.database.BudgetDatabase;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.entity.TransactionType;
import com.example.budgetapp.exceptions.CannotVerifyDataException;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AccountRepo {
    private static final String TAG = "AccountRepo";
    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private LiveData<List<Account>> allAccounts;
    private LiveData<List<String>> allAccountNames;
    private LiveData<BigDecimal> balanceSum;
    private LiveData<Account> activeAccount;
    private ExecutorService executorService;

    public AccountRepo(Application application){
        accountDao = BudgetDatabase.getInstance(application).accountDao();
        transactionDao = BudgetDatabase.getInstance(application).transactionDao();
        allAccounts = accountDao.getAllAccounts();
        allAccountNames = accountDao.getAccountNames();
        balanceSum = accountDao.getSumOfAccountBalance();
        activeAccount = accountDao.getActiveAccount();
        executorService = Executors.newSingleThreadExecutor();
    }


    public void insertOne(Account account){
        executorService.execute(() -> accountDao.insertOne(account));
    }
    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }

    public LiveData<List<String>> getAllAccountNames() {
        return allAccountNames;
    }
    public LiveData<BigDecimal> getBalanceSum(){
        return balanceSum;
    }

    public LiveData<Account> getActiveAccount(){
        return activeAccount;
    }
    public void setActiveAccount(String name){
        executorService.execute(() -> {
            List<Account> accounts = allAccounts.getValue();
            for (Account a: accounts) {
                if(a.getAccountName().equals(name))
                    a.setActive(true);
                else
                    a.setActive(false);
                accountDao.updateOne(a);
            }
        });
    }
    public Boolean accountNameExist(String accountName) {
        Future<Account> future = executorService.submit(() -> accountDao.getAccountByName(accountName));
        try {
            Account account = future.get(2, TimeUnit.SECONDS); // Timeout after 2 seconds
            return account != null;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new CannotVerifyDataException("Cannot tell if account name exists.", e);
        }
    }
    public BigDecimal getCurrentBalanceOf(String name){
        Future<BigDecimal> currentBalance = executorService.submit(()->{
            Account account = accountDao.getAccountByName(name);
            BigDecimal starting = account.getStartingBalance();
            BigDecimal income = transactionDao.getSumOfTypeFrom(TransactionType.INCOME, name);
            BigDecimal expense = transactionDao.getSumOfTypeFrom(TransactionType.EXPENSE, name);

            return starting.add(income).subtract(expense);
        });
        try{
            return currentBalance.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e){
            throw new CannotVerifyDataException("Sum cannot be found.", e);
        }
    }
    public void deleteByName(String name){
        executorService.execute(() -> {
            List<Account> accounts = allAccounts.getValue();
            Log.d(TAG, "deleteByName->run: Name to delete is " + name);
            for (Account a: accounts) {
                if(a.getAccountName().equals(name)){
                    accountDao.deleteOne(a);
                    break;
                }
            }
        });
    }
}
