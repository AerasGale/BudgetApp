package com.example.budgetapp.fragments.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetapp.account.Account;
import com.example.budgetapp.exceptions.CannotVerifyDataException;
import com.example.budgetapp.account.AccountRepo;

import java.math.BigDecimal;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final AccountRepo accountRepo;
    private final LiveData<List<Account>> accounts;
    private final LiveData<BigDecimal> balanceSum;
    private final MutableLiveData<String> toastMessage;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        accountRepo = new AccountRepo(application);
        accounts = accountRepo.getAllAccounts();
        balanceSum = accountRepo.getBalanceSum();
        toastMessage = new MutableLiveData<>();
    }

    public void createAccount(String accountName, BigDecimal startingBalance, int iconResId){
        if(accountName.trim().length()==0){
            toastMessage.setValue("Enter an account name.");
            return;
        }
        try{
            if(accountRepo.accountNameExist(accountName)){
                toastMessage.setValue("Account names cannot repeat.");
                return;
            }
        } catch (CannotVerifyDataException e){
            toastMessage.setValue(e.getMessage());
        }
        accountRepo.createAndInsertAccount(accountName, startingBalance, iconResId);
    }
    public void deleteAccountByName(String name){
        accountRepo.deleteByName(name);
    }

    public LiveData<List<Account>> getAllAccounts() {
        return accounts;
    }

    public LiveData<BigDecimal> getBalanceSum() {
        return balanceSum;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
}
