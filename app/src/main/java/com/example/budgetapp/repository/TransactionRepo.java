package com.example.budgetapp.repository;

import androidx.lifecycle.LiveData;

import com.example.budgetapp.repository.dao.TransactionDao;
import com.example.budgetapp.entity.Transaction;

import java.util.List;

public class TransactionRepo {
    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;
}
