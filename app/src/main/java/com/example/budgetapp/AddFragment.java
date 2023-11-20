package com.example.budgetapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.database.BudgetDatabase;
import com.example.budgetapp.databinding.FragmentAddBinding;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.repository.AccountRepo;
import com.example.budgetapp.viewmodel.AccountViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddFragment extends Fragment {
    private static final String TAG = "AddFragment";
    private AccountViewModel accountViewModel;
    private FragmentAddBinding binding;
    private List<String> accountNames;
    private Account activeAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner accSelector = binding.spnAccount;
        Button addButton = binding.btnAddTransaction;

        // ViewModel Listeners
        accountViewModel = new ViewModelProvider(this.getActivity()).get(AccountViewModel.class);
        accountNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, accountNames);
        accountViewModel.getAllAccountNames().observe(getViewLifecycleOwner(), strings -> {
            for (String s : strings) {
                if (!accountNames.contains(s)){
                    accountNames.add(s);
                }
            }
            adapter.notifyDataSetChanged();
            Log.d(TAG, "onChanged: Account names are " + strings);
        });
        accountViewModel.getActiveAccount().observe(getViewLifecycleOwner(), account -> activeAccount = account);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accSelector.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etAmount = binding.etAmount;
                RadioGroup rgTransactionType = binding.rgTransactionType;


            }
        });
        accSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getSelectedItem().toString();
                if(selected!=null){
                    Toast.makeText(parent.getContext(), "Selected " + selected, Toast.LENGTH_SHORT).show();
                    accountViewModel.setActiveAccount(selected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

}