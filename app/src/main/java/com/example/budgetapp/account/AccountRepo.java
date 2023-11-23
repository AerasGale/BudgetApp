package com.example.budgetapp.account;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.budgetapp.transaction.TransactionDao;
import com.example.budgetapp.database.BudgetDatabase;
import com.example.budgetapp.transaction.TransactionType;
import com.example.budgetapp.exceptions.CannotVerifyDataException;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AccountRepo {
    private static final String TAG = "AccountRepo";
    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private LiveData<List<Account>> allAccounts;
    private LiveData<List<String>> allAccountNames;
    private LiveData<BigDecimal> balanceSum;
    private ExecutorService executorService;

    public AccountRepo(Application application){
        accountDao = BudgetDatabase.getInstance(application).accountDao();
        transactionDao = BudgetDatabase.getInstance(application).transactionDao();
        allAccounts = accountDao.getAllAccounts();
        allAccountNames = accountDao.getAccountNames();
        balanceSum = accountDao.getSumOfAccountBalance();
        executorService = Executors.newSingleThreadExecutor();
    }


    private void insertOne(Account account){
        executorService.execute(() -> accountDao.insertOne(account));
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

    public Boolean accountNameExist(String accountName) {
        Future<Account> future = executorService.submit(() -> accountDao.getAccountByName(accountName));
        try {
            Account account = future.get(2, TimeUnit.SECONDS); // Timeout after 2 seconds
            return account != null;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new CannotVerifyDataException("Cannot tell if account name exists.", e);
        }
    }
    private BigDecimal getCurrentBalanceOf(String name){
        Future<BigDecimal> currentBalance = executorService.submit(()->{
            Account account = accountDao.getAccountByName(name);
            BigDecimal starting = account.getStartingBalance();
            BigDecimal income = transactionDao.getSumOfTypeFrom(TransactionType.INCOME, name);
            BigDecimal expense = transactionDao.getSumOfTypeFrom(TransactionType.EXPENSE, name);

            return starting.add(income).subtract(expense);
        });
        try{
            return currentBalance.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e){
            throw new CannotVerifyDataException("Sum cannot be found.", e);
        }
    }
    public void createAndInsertAccount(String accountName, BigDecimal startingBalance, int iconResId){
        Account accountToAdd = new Account(accountName, startingBalance,iconResId);
        insertOne(accountToAdd);
    }
    public void deleteByName(String name){
        executorService.execute(() -> {
            List<Account> accounts = allAccounts.getValue();
            Log.d(TAG, "deleteByName->run: Name to delete is " + name);
            for (Account a: accounts) {
                if(a.getAccountName().equals(name)){
                    accountDao.deleteOne(a);
                    break;
                }
            }
        });
    }

    public void updateBalance(String accountName) {
        Future<Account> accountFuture = executorService.submit(() -> accountDao.getAccountByName(accountName));

        try{
            Account accountToUpdate = accountFuture.get(2, TimeUnit.SECONDS);
            Future<BigDecimal> futureCurrentBalance = executorService.submit(()->{
                BigDecimal starting = accountToUpdate.getStartingBalance();
                BigDecimal income = transactionDao.getSumOfTypeFrom(TransactionType.INCOME, accountName);
                BigDecimal expense = transactionDao.getSumOfTypeFrom(TransactionType.EXPENSE, accountName);
                return starting.add(income).subtract(expense);
            });
            try{
                BigDecimal currentBalance = futureCurrentBalance.get(2, TimeUnit.SECONDS);
                executorService.execute(()-> {
                    accountToUpdate.setAccountBalance(currentBalance);
                    accountDao.updateOne(accountToUpdate);
                });
            } catch (InterruptedException | ExecutionException | TimeoutException e){
                throw new CannotVerifyDataException("Sum cannot be found.", e);
            }
        }catch (InterruptedException | ExecutionException | TimeoutException e){
            throw new CannotVerifyDataException("Cannot find account " + accountName, e);
        }


    }
}
