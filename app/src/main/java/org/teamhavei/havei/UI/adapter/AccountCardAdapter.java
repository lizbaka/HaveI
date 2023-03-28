package org.teamhavei.havei.UI.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.event.BookAccount;
import org.teamhavei.havei.R;
import org.teamhavei.havei.util.IconManager;
import org.teamhavei.havei.util.Util;
import org.teamhavei.havei.database.BookkeepDBHelper;

import java.util.List;

public class AccountCardAdapter extends RecyclerView.Adapter<AccountCardAdapter.ViewHolder> {

    public interface Callback {
        void onAccountSelected(BookAccount bookAccount);
    }

    Callback callback;

    List<BookAccount> accountList;
    Context context;
    BookkeepDBHelper dbHelper;

    public AccountCardAdapter(Context context, List<BookAccount> accountList, Callback callback) {
        this.context = context;
        this.accountList = accountList;
        this.callback = callback;
        dbHelper = new BookkeepDBHelper(context, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_bookkeep_account_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAccountSelected(holder.account);
            }
        });
        return holder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.account = accountList.get(position);
        if(holder.account.getId() == Util.DEFAULT_ACCOUNT_ID){
            holder.accountBalanceTV.setVisibility(View.GONE);
        }else{
            holder.accountBalanceTV.setVisibility(View.VISIBLE);
        }
        holder.accountIconIV.setImageDrawable(new IconManager(context).getIcon(holder.account.getIconId()));
        holder.accountNameTV.setText(holder.account.getName());
        holder.accountBalanceTV.setText(String.format("%.2f",holder.account.getInit()+dbHelper.getPMByAccount(holder.account.getId())));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView accountIconIV;
        TextView accountNameTV;
        TextView accountBalanceTV;
        BookAccount account;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            accountIconIV = itemView.findViewById(R.id.account_icon);
            accountNameTV = itemView.findViewById(R.id.account_name);
            accountBalanceTV = itemView.findViewById(R.id.account_balance);
        }

    }

}
