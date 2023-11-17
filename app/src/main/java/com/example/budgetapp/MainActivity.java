package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.budgetapp.database.BudgetDatabase;
import com.example.budgetapp.databinding.ActivityMainBinding;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.repository.AccountRepo;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private AccountRepo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.bottomAppBar;
        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.searchFragment, R.id.addFragment,R.id.recordsFragment,R.id.settingsFragment).build();
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        repo = new AccountRepo(this.getApplication());
        Log.d(TAG, "onCreate: AllAccountNames are:" + repo.getAllAccounts().getValue());


//        ((NavigationBarView)binding.bottomNavigationView).setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                navController.navigate(item.getItemId());
//                return true;
//            }
//        });
    }

}