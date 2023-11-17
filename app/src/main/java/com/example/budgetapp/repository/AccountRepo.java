package com.example.budgetapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.budgetapp.dao.AccountDao;
import com.example.budgetapp.database.BudgetDatabase;
import com.example.budgetapp.entity.Account;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountRepo {
    private static final String TAG = "AccountRepo";
    private AccountDao accountDao;
    private LiveData<List<Account>> allAccounts;
    private LiveData<List<String>> allAccountNames;
    private LiveData<Account> activeAccount;
    private ExecutorService executorService;

    public AccountRepo(Application application){
        accountDao = BudgetDatabase.getInstance(application).accountDao();
        allAccounts = accountDao.getAllAccounts();
        allAccountNames = accountDao.getAccountNames();
        activeAccount = accountDao.getActiveAccount();
        executorService = Executors.newSingleThreadExecutor();
    }


    public void insertOne(Account account){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                accountDao.insertOne(account);
            }
        });
    }
    public void updateOne(Account account){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                accountDao.updateOne(account);
            }
        });
    }
    public void deleteOne(Account account){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                accountDao.deleteOne(account);
            }
        });
    }

    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }

    public LiveData<List<String>> getAllAccountNames() {
        return allAccountNames;
    }
    public LiveData<Account> getAccountByName(String name){
        LiveData<Account> acc =accountDao.getAccountByName(name);
        String logmsg = "Account is not null";
        if(acc == null){
            logmsg = "Account is null";
        }
        Log.d(TAG, "getAccountByName: " + logmsg);

        return acc;
    }
    public LiveData<Account> getActiveAccount(){
        return activeAccount;
    }
    public void setActiveAccount(String name){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Account> accounts = allAccounts.getValue();
                for (Account a: accounts) {
                    if(a.getAccountName().equals(name))
                        a.setActive(true);
                    else
                        a.setActive(false);
                    accountDao.updateOne(a);
                }
            }
        });
    }

    public void deleteByName(String name){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Account> accounts = allAccounts.getValue();
                for (Account a: accounts) {
                    if(a.getAccountName().equals(name)){
                        accountDao.deleteOne(a);
                        break;
                    }
                }
            }
        });

    }
}
