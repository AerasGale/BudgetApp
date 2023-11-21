package com.example.budgetapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetapp.entity.Account;
import com.example.budgetapp.repository.AccountRepo;

import java.math.BigDecimal;
import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepo repo;
    private LiveData<List<Account>> allAccounts;
    private LiveData<List<String>> allAccountNames;
    LiveData<BigDecimal> balanceSum;
    private LiveData<Account> activeAccount;
    public AccountViewModel(@NonNull Application application) {
        super(application);
        repo = new AccountRepo(application);
        allAccounts = repo.getAllAccounts();
        allAccountNames = repo.getAllAccountNames();
        balanceSum = repo.getBalanceSum();
        activeAccount = repo.getActiveAccount();
    }

    public void insertOne(Account account){
        repo.insertOne(account);
    }
    public void deleteByName(String name){
        repo.deleteByName(name);
    }
    public void setActiveAccount(String accName){
        repo.setActiveAccount(accName);
    }
    public  LiveData<Account> getActiveAccount(){
        return activeAccount;
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
    public LiveData<Account> getAccountByName(String name){
        LiveData<Account> acc =  repo.getAccountByName(name);
        return acc;
    }
}
