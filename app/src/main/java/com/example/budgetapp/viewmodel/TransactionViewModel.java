package com.example.budgetapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetapp.entity.Transaction;
import com.example.budgetapp.entity.TransactionType;
import com.example.budgetapp.repository.AccountRepo;
import com.example.budgetapp.repository.TransactionRepo;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    private TransactionRepo transactionRepo;
    private LiveData<List<Transaction>> allTransactions;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
    }
    public void createTransaction(String accountName, TransactionType transactionType, BigDecimal amount){
        Transaction transaction = new Transaction(accountName, transactionType, ZonedDateTime.now(), amount);

    }
}
