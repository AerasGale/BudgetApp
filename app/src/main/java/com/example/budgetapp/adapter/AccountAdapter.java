package com.example.budgetapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.R;
import com.example.budgetapp.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private List<Account> accounts = new ArrayList<>();

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_account, parent, false);
        return new AccountViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account current = accounts.get(position);
        holder.accountIcon.setImageResource(current.getIconResId());
        holder.accountName.setText(current.getAccountName());
        holder.accountBalance.setText(current.getAccountBalance().toString());
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void setAccounts(List<Account> accounts){
        this.accounts = accounts;
        notifyDataSetChanged();
    }
    public static class AccountViewHolder extends RecyclerView.ViewHolder{
        private ImageView accountIcon;
        private TextView accountName;
        private TextView accountBalance;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            accountIcon = itemView.findViewById(R.id.accountIcon);
            accountName =  itemView.findViewById(R.id.accountName);
            accountBalance =  itemView.findViewById(R.id.accountBalance);
        }
    }
}
