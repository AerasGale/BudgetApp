package com.example.budgetapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment implements RecyclerViewInterface {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private AccountViewModel accountViewModel;
    private List<String> accountNames;
    private AccountAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerAccounts = setUpRecyclerView();

        accountViewModel = new ViewModelProvider(this.requireActivity()).get(AccountViewModel.class);
        accountViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {
            adapter.setAccounts(accounts);
            adapter.notifyDataSetChanged();
            accountNames = new ArrayList<>();
            for(Account a: this.adapter.getAccounts()){
                accountNames.add(a.getAccountName());
            }
        });
        Button btnAddPopup = binding.btnAddPopup;
        btnAddPopup.setOnClickListener(v -> showPopupWindow(v));

        return root;
    }

    private RecyclerView setUpRecyclerView(){
        RecyclerView recyclerAccounts = binding.recyclerAccounts;
        recyclerAccounts.setLayoutManager( new LinearLayoutManager(this.getContext()));
        recyclerAccounts.setHasFixedSize(true);

        adapter = new AccountAdapter(this.getContext(), this);
        recyclerAccounts.setAdapter(adapter);
        return recyclerAccounts;
    }
    private void showPopupWindow(View v){
        View popupView = setupPopupView(v);
        AtomicInteger iconResId = new AtomicInteger();
        PopupWindow popupWindow = createPopupWindow(popupView, v.getRootView());
        setupSpinner(popupView, iconResId);
        setupAddAccountButton(popupView, popupWindow, iconResId);
    }

    private View setupPopupView(View v){
        LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.popup_add_account, null);
    }
    private PopupWindow createPopupWindow(View popupView, View rootView){
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0,0);
        return  popupWindow;
    }
    private void setupSpinner(View popupView, AtomicInteger iconResId){
        Spinner spnIcon = popupView.findViewById(R.id.spnIcon);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(popupView.getContext(), R.array.default_account_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIcon.setAdapter(adapter);
        spnIcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getSelectedItem().toString()){
                    case "Bank":
                        iconResId.set(R.drawable.ic_bank);
                        break;
                    case "Cash":
                        iconResId.set(R.drawable.ic_cash);
                        break;
                    case "Card":
                        iconResId.set(R.drawable.ic_card);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setupAddAccountButton(View popupView, PopupWindow popupWindow, AtomicInteger iconResId){
        Button btnAddAccount = popupView.findViewById(R.id.btnAddAccount);
        EditText etAccountName = popupView.findViewById(R.id.etAccountName);
        EditText etStartingBalance = popupView.findViewById(R.id.etStartingBalance);
        btnAddAccount.setOnClickListener(v1 -> {
            for(Account a: this.adapter.getAccounts()){
                accountNames.add(a.getAccountName());
            }

            if(accountNames==null){
                Toast.makeText(v1.getContext(), "accountNames is null", Toast.LENGTH_SHORT).show();
                return;
            }
            if(etAccountName.getText().length()<=0){
                Toast.makeText(v1.getContext(), "Enter an account name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(accountNames.contains(etAccountName.getText().toString())){
                Toast.makeText(v1.getContext(), "Account names cannot repeat.", Toast.LENGTH_SHORT).show();
                return;
            }
            Account accountToAdd = new Account(etAccountName.getText().toString(), new BigDecimal(etStartingBalance.getText().toString()),iconResId.get(),false);
            accountViewModel.insertOne(accountToAdd);
            popupWindow.dismiss();
        });
    }

    @Override
    public void onItemLongClick(int position) {
        Log.d(TAG, "onItemLongClick: Current list of names are " + accountNames);
        if(accountNames!=null){
            String accName = accountNames.get(position);
            Toast.makeText(this.getContext(), "Item in position " + position + " is called " + accName, Toast.LENGTH_SHORT).show();


            accountViewModel.deleteByName(accountNames.get(position));

        }
    }
}