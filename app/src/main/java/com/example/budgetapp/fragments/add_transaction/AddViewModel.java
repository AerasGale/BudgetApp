package com.example.budgetapp.fragments.add_transaction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetapp.account.Account;
import com.example.budgetapp.transaction.TransactionType;
import com.example.budgetapp.account.AccountRepo;
import com.example.budgetapp.transaction.TransactionRepo;

import java.math.BigDecimal;
import java.util.List;

public class AddViewModel extends AndroidViewModel {
    private AccountRepo accountRepo;
    private TransactionRepo transactionRepo;
    private LiveData<List<String>> accountNames;
    private MutableLiveData<Account> selectedAccount;

    public AddViewModel(@NonNull Application application) {
        super(application);
        accountRepo = new AccountRepo(application);
        transactionRepo = new TransactionRepo(application);
        accountNames = accountRepo.getAllAccountNames();
        selectedAccount = new MutableLiveData<>();
    }
    public LiveData<List<String>> getAllAccountNames() {
        return accountNames;
    }
    public void createTransaction(String accountName, BigDecimal amount, TransactionType type){
        transactionRepo.createAndInsertTransaction(accountName,amount,type);
        accountRepo.updateBalance(accountName);
    }
}
