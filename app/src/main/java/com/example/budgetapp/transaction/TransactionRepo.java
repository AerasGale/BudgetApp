package com.example.budgetapp.transaction;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.budgetapp.database.BudgetDatabase;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionRepo {
    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;
    private LiveData<List<Transaction>> allIncomes;
    private LiveData<List<Transaction>> allExpenses;
    private ExecutorService executorService;

    public TransactionRepo(Application application){
        transactionDao = BudgetDatabase.getInstance(application).transactionDao();
        allTransactions = transactionDao.getAllTransactions();
        allIncomes = transactionDao.getTransactionsOfType(TransactionType.INCOME);
        allExpenses = transactionDao.getTransactionsOfType(TransactionType.EXPENSE);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }
    public LiveData<List<Transaction>> getAllIncomes() {
        return allIncomes;
    }

    public LiveData<List<Transaction>> getAllExpenses() {
        return allExpenses;
    }
    private void insertOne(Transaction transaction){
        executorService.execute(() -> transactionDao.insertOne(transaction));
    }
    public void updateOne(Transaction transaction){
        executorService.execute(()-> transactionDao.updateOne(transaction));
    }
    public void deleteOne(Transaction transaction){
        executorService.execute(()-> transactionDao.deleteOne(transaction));
    }
    public void createAndInsertTransaction(String accountName, BigDecimal amount, TransactionType type){
        Transaction transactionToInsert = new Transaction(accountName,type, ZonedDateTime.now(),amount);
        insertOne(transactionToInsert);
    }

}
