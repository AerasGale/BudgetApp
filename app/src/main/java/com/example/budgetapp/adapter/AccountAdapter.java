package com.example.budgetapp.adapter;

import android.content.Context;
import android.util.Log;
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
    private final Context context;
    private final RecyclerViewInterface recyclerViewInterface;
    private List<Account> accounts = new ArrayList<>();

    public AccountAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_account, parent, false);
        return new AccountViewHolder(itemView, recyclerViewInterface);
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
    }
    public List<Account> getAccounts(){return this.accounts;}
    public static class AccountViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "AccountViewHolder";
        private ImageView accountIcon;
        private TextView accountName;
        private TextView accountBalance;

        public AccountViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            accountIcon = itemView.findViewById(R.id.accountIcon);
            accountName =  itemView.findViewById(R.id.accountName);
            accountBalance =  itemView.findViewById(R.id.accountBalance);

            itemView.setOnLongClickListener(v -> {
                if(recyclerViewInterface != null){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemLongClick(pos);
                    }
                    Log.d(TAG, "onLongClick: Selected position" + pos);
                }
                return true;
            });
        }
    }
}
