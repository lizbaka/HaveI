package org.teamhavei.havei.UI.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.event.BookAccount;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UI.adapter.AccountCardAdapter;
import org.teamhavei.havei.database.BookkeepDBHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentBookkeepAccountList extends BottomSheetDialogFragment {

    String operationName;

    public FragmentBookkeepAccountList(Callback callback,String operationName){
        this.callback = callback;
        this.operationName = operationName;
    }

    public interface Callback {
        void onBookAccountSelected(BookAccount bookAccount);
        void operate();
    }

    Callback callback;

    RecyclerView accountListRV;
    TextView operationTV;
    MaterialCardView operateMCV;

    List<BookAccount> accountList = new ArrayList<>();
    BookkeepDBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new BookkeepDBHelper(getContext(),BookkeepDBHelper.DB_NAME,null,BookkeepDBHelper.DATABASE_VERSION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookkeep_account_list, container, false);
        initView(view);
        accountList = dbHelper.findAllBookAccount();
        accountListRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        accountListRV.setAdapter(new AccountCardAdapter(getContext(), accountList, new AccountCardAdapter.Callback() {
            @Override
            public void onAccountSelected(BookAccount bookAccount) {
                callback.onBookAccountSelected(bookAccount);
            }
        }));
        operateMCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.operate();
            }
        });
        operationTV.setText(operationName);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        accountList.clear();
        accountList.addAll(dbHelper.findAllBookAccount());
        accountListRV.getAdapter().notifyDataSetChanged();
    }

    private void initView(View view){
        accountListRV = view.findViewById(R.id.bookkeep_account_list);
        operationTV = view.findViewById(R.id.bookeep_account_list_operation);
        operateMCV = view.findViewById(R.id.bookkeep_account_list_operate);
    }
}
