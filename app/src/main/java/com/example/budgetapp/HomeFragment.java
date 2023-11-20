package com.example.budgetapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.budgetapp.adapter.AccountAdapter;
import com.example.budgetapp.adapter.RecyclerViewInterface;
import com.example.budgetapp.databinding.FragmentHomeBinding;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.viewmodel.AccountViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface {
    private FragmentHomeBinding binding;
    private AccountViewModel accountViewModel;
    private List<String> accountNames;
    private AccountAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerAccounts = binding.recyclerAccounts;
        recyclerAccounts.setLayoutManager( new LinearLayoutManager(this.getContext()));
        recyclerAccounts.setHasFixedSize(true);

        adapter = new AccountAdapter(this.getContext(), this);
        recyclerAccounts.setAdapter(adapter);

        accountViewModel = new ViewModelProvider(this.getActivity()).get(AccountViewModel.class);
        accountNames = new ArrayList<>();
        accountViewModel.getAllAccounts().observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                adapter.setAccounts(accounts);

            }
        });
        accountViewModel.getAllAccountNames().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                for (String s : strings) {
                    if (!accountNames.contains(s)){
                        accountNames.add(s);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        Button btnAddPopup = binding.btnAddPopup;
        btnAddPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePopupWindow(v);
            }
        });



        return root;
    }

    private void CreatePopupWindow(View v){
        LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_add_account, binding.getRoot());

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0,0);

        EditText etAccountName = popupView.findViewById(R.id.etAccountName);
        EditText etStartingBalance = popupView.findViewById(R.id.etStartingBalance);
        Spinner spnIcon = popupView.findViewById(R.id.spnIcon);
        Button btnAddAccount = popupView.findViewById(R.id.btnAddAccount);

        etAccountName.requestFocus();
        
        final int[] iconResId = new int[1];

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.default_account_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIcon.setAdapter(adapter);
        spnIcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getSelectedItem().toString()){
                    case "Bank":
                        iconResId[0] = R.drawable.ic_bank;
                        break;
                    case "Cash":
                        iconResId[0] = R.drawable.ic_cash;
                        break;
                    case "Card":
                        iconResId[0] = R.drawable.ic_card;
                }
                Toast.makeText(v.getContext(), iconResId[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAccountName.getText().length()<=0){
                    Toast.makeText(v.getContext(), "Enter an account name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Account accountToAdd = new Account(etAccountName.getText().toString(), new BigDecimal(etStartingBalance.getText().toString()),iconResId[0],false);
                accountViewModel.insertOne(accountToAdd);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onItemLongClick(int position) {
        accountViewModel.deleteByName(accountNames.get(position));
    }
}