package com.example.budgetapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetapp.entity.Account;
import com.example.budgetapp.repository.AccountRepo;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private static final String TAG = "AccountViewModel";
    private AccountRepo repo;
    private LiveData<List<Account>> allAccounts;
    private LiveData<List<String>> allAccountNames;
    private LiveData<Account> activeAccount;
    public AccountViewModel(@NonNull Application application) {
        super(application);
        repo = new AccountRepo(application);
        allAccounts = repo.getAllAccounts();
        allAccountNames = repo.getAllAccountNames();
        activeAccount = repo.getActiveAccount();
    }

    public void insertOne(Account account){
        repo.insertOne(account);
    }
    public void updateOne(Account account){
        repo.updateOne(account);
    }
    public void deleteOne(Account account){
        repo.deleteOne(account);
    }
    public void setActiveAccount(String accName){
        repo.setActiveAccount(accName);
    }
    public  LiveData<Account> getActiveAccount(){
        return activeAccount;
    }
    public boolean isActiveNull(){
        if(activeAccount==null)
            return true;
        else
            return false;
    }
    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }

    public LiveData<List<String>> getAllAccountNames() {
        return allAccountNames;
    }

    public LiveData<Account> getAccountByName(String name){
        LiveData<Account> acc =  repo.getAccountByName(name);
        String logmsg = "Account is not null";
        if(acc == null){
            logmsg = "Account is null";
        }
        Log.d(TAG, "getAccountByName: " + logmsg);
        return acc;
    }

}
