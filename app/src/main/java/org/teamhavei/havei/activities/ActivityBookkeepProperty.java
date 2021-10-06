package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.Event.BookAccount;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.AccountCardAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.util.List;

public class ActivityBookkeepProperty extends AppCompatActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBookkeepProperty.class);
        context.startActivity(intent);
    }

    RecyclerView accountRV;

    BookkeepDBHelper dbHelper;
    List<BookAccount> accountList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_property);
        setSupportActionBar(findViewById(R.id.bookkeep_property_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new BookkeepDBHelper(ActivityBookkeepProperty.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        accountList = dbHelper.findAllBookAccount();

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_1, menu);
        menu.getItem(0).setIcon(R.drawable.ic_baseline_refresh_24);
        menu.getItem(0).setTitle(R.string.bookkeep_property_clear);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_1_0:
                update(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        accountRV = findViewById(R.id.bookkeep_property_account_list);

        accountRV.setLayoutManager(new LinearLayoutManager(ActivityBookkeepProperty.this, LinearLayoutManager.VERTICAL, false));
        accountRV.setAdapter(new AccountCardAdapter(ActivityBookkeepProperty.this, accountList, new AccountCardAdapter.Callback() {
            @Override
            public void onAccountSelected(BookAccount bookAccount) {
                update(bookAccount);
            }
        }));
    }

    private void update(BookAccount account) {
        if (account == null) {

        } else {

        }
    }
}