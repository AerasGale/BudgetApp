package com.example.budgetapp.fragments.add_transaction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.budgetapp.databinding.FragmentAddTransactionBinding;
import com.example.budgetapp.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class AddTransactionFragment extends Fragment {
    private static final String TAG = "AddFragment";
    private static final String SPINNER_SELECTED_ITEM = "arg_selected_item";
    private AddViewModel addViewModel;
    private FragmentAddTransactionBinding binding;
    private LiveData<List<String>> accountNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner accSelector = binding.spnAccount;
        Button addButton = binding.btnAddTransaction;

        addViewModel = new ViewModelProvider(this.getActivity()).get(AddViewModel.class);
        accountNames = addViewModel.getAllAccountNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accSelector.setAdapter(adapter);
        if(savedInstanceState!= null){
            accSelector.setSelection(savedInstanceState.getInt(SPINNER_SELECTED_ITEM, 0));
        }


        // Listeners
        addViewModel.getAllAccountNames().observe(getViewLifecycleOwner(), strings -> {
            adapter.clear();
            adapter.addAll(strings);
            adapter.notifyDataSetChanged();
            Log.d(TAG, "onChanged: Account names are " + strings);
        });

        addButton.setOnClickListener(v -> {
            EditText etAmount = binding.etAmount;
            Spinner spnAccount = binding.spnAccount;
            RadioGroup rgTransactionType = binding.rgTransactionType;

            BigDecimal amount = new BigDecimal(etAmount.getText().toString());
            TransactionType transactionType;

            if(rgTransactionType.getCheckedRadioButtonId() == binding.rbIncome.getId()) {
                transactionType = TransactionType.INCOME;
            } else if (rgTransactionType.getCheckedRadioButtonId() == binding.rbExpense.getId()) {
                transactionType = TransactionType.EXPENSE;
            } else {
                return;
            }
            addViewModel.createTransaction(accSelector.getSelectedItem().toString(), amount, transactionType);
        });
        accSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getSelectedItem().toString();
                if(selected!=null){
                    Toast.makeText(parent.getContext(), "Selected " + selected, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }
}