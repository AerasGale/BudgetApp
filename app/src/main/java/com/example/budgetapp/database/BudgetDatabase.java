package com.example.budgetapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.budgetapp.R;
import com.example.budgetapp.dao.AccountDao;
import com.example.budgetapp.dao.TransactionDao;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.entity.Transaction;
import com.example.budgetapp.utils.roomutils.RoomTypeConverters;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Account.class, Transaction.class}, version = 1)
@TypeConverters({RoomTypeConverters.class})
public abstract class BudgetDatabase extends RoomDatabase {
    private static final String TAG = "BudgetDatabase";
    public abstract AccountDao accountDao();
    public abstract TransactionDao transactionDao();
    private static volatile BudgetDatabase INSTANCE;
    public static BudgetDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (BudgetDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BudgetDatabase.class, "budget_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                    Log.d(TAG, "getInstance: Instance created");
//                    new TriggerCreateAsyncTask(INSTANCE).execute();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> INSTANCE.query("select 1", null));
                }

            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d(TAG, "onCreate: Callback called");

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> INSTANCE.preloadData(INSTANCE.accountDao()));
        }
    };
    private void preloadData(AccountDao accountDao){
        accountDao.insertOne(new Account("Bank", BigDecimal.ZERO, R.drawable.ic_bank, true));
        accountDao.insertOne(new Account("Card", BigDecimal.ZERO, R.drawable.ic_cash, false));
        accountDao.insertOne(new Account("Cash", BigDecimal.ZERO, R.drawable.ic_card, false));
    }

}
