package com.example.budgetapp.fragments.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.R;
import com.example.budgetapp.interfaces.RecyclerViewInterface;
import com.example.budgetapp.databinding.FragmentHomeBinding;
import com.example.budgetapp.account.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment implements RecyclerViewInterface {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private List<String> accountNames;
    private AccountAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();

        homeViewModel = new ViewModelProvider(this.requireActivity()).get(HomeViewModel.class);
        homeViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {




            adapter.setAccounts(accounts);
            adapter.notifyDataSetChanged();
            accountNames = new ArrayList<>();
            for(Account a: this.adapter.getAccounts()){
                accountNames.add(a.getAccountName());
            }
        });
        homeViewModel.getToastMessage().observe(getViewLifecycleOwner(), s -> Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT).show());
        TextView tvBalanceSum = binding.tvBalanceSum;
        homeViewModel.getBalanceSum().observe(getViewLifecycleOwner(), bigDecimal -> {
            if(bigDecimal != null){
                tvBalanceSum.setText(bigDecimal.toString());
            }
        });
        Button btnAddPopup = binding.btnAddPopup;
        btnAddPopup.setOnClickListener(this::showPopupWindow);
    }

    private void setUpRecyclerView(){
        RecyclerView recyclerAccounts = binding.recyclerAccounts;
        recyclerAccounts.setLayoutManager( new LinearLayoutManager(this.getContext()));
        recyclerAccounts.setHasFixedSize(true);

        adapter = new AccountAdapter(this.getContext(), this);
        recyclerAccounts.setAdapter(adapter);
    }
    private void showPopupWindow(View v){
        View popupView = setupPopupView(v);
        AtomicInteger iconResId = new AtomicInteger();
        PopupWindow popupWindow = createPopupWindow(popupView, v.getRootView());
        setupSpinner(popupView, iconResId);
        setupAddAccountButton(popupView, popupWindow, iconResId);
    }

    @SuppressLint("InflateParams")
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

        etStartingBalance.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                v.post(()-> ((EditText)v).selectAll());
            }
        });
        btnAddAccount.setOnClickListener(v -> {
            homeViewModel.createAccount(etAccountName.getText().toString(), new BigDecimal(etStartingBalance.getText().toString()),iconResId.get());
            popupWindow.dismiss();
        });
    }

    @Override
    public void onItemLongClick(int position) {
        Log.d(TAG, "onItemLongClick: Current list of names are " + accountNames);
        if(accountNames!=null){
            String accName = accountNames.get(position);
            Toast.makeText(this.getContext(), "Item in position " + position + " is called " + accName, Toast.LENGTH_SHORT).show();


            homeViewModel.deleteAccountByName(accountNames.get(position));

        }
    }
}