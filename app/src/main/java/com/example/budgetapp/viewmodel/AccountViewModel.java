package com.example.budgetapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetapp.entity.Account;
import com.example.budgetapp.exceptions.CannotVerifyDataException;
import com.example.budgetapp.repository.AccountRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepo repo;
    private LiveData<List<Account>> allAccounts;
    private LiveData<List<String>> allAccountNames;
    private LiveData<BigDecimal> balanceSum;
    private LiveData<Account> activeAccount;
    private MutableLiveData<String> toastMessage;
    public AccountViewModel(@NonNull Application application) {
        super(application);
        repo = new AccountRepo(application);
        allAccounts = repo.getAllAccounts();
        allAccountNames = repo.getAllAccountNames();
        balanceSum = repo.getBalanceSum();
        activeAccount = repo.getActiveAccount();
        toastMessage = new MutableLiveData<>();
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
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
    public LiveData<BigDecimal> getBalanceSum(){
        return balanceSum;
    }
    public void createAccount(String accountName, BigDecimal startingBalance, int iconResId, LifecycleOwner lifecycleOwner){
        if(accountName.trim().length()==0){
            toastMessage.setValue("Enter an account name.");
            return;
        }
        try{
            if(repo.accountNameExist(accountName)){
                toastMessage.setValue("Account names cannot repeat.");
                return;
            }
        } catch (CannotVerifyDataException e){
            toastMessage.setValue(e.getMessage());
        }

        Account accountToAdd = new Account(accountName, startingBalance,iconResId,false);
        repo.insertOne(accountToAdd);

    }
}
