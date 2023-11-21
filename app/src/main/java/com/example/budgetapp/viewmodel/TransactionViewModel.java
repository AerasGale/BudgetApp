package com.example.budgetapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetapp.entity.Transaction;
import com.example.budgetapp.repository.TransactionRepo;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    private TransactionRepo repo;
    private LiveData<List<Transaction>> allTransactions;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
    }
}
