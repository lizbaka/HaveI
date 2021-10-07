package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import org.teamhavei.havei.Event.BookAccount;
import org.teamhavei.havei.Event.singleLineManager;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.AccountCardAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityBookkeepProperty extends BaseActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBookkeepProperty.class);
        context.startActivity(intent);
    }

    RecyclerView accountRV;

    BookkeepDBHelper dbHelper;
    List<BookAccount> accountList;
    ArrayList<Double> balanceData;
    LineChart line_two;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_property);
        setSupportActionBar(findViewById(R.id.bookkeep_property_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new BookkeepDBHelper(ActivityBookkeepProperty.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        accountList = dbHelper.findAllBookAccount();
        balanceData = new ArrayList<>();
        initView();
        update(null);

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
        line_two=findViewById(R.id.chart_line2);
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
            balanceData.clear();
            balanceData.addAll(dbHelper.getBalanceListFor12Months());
        } else {
            balanceData.clear();
            balanceData.addAll(dbHelper.getBalanceListFor12Months(account.getId()));
        }
        setmySINGLE(line_two);
    }
    public void setmySINGLE(LineChart mLine)
    {
        ArrayList<Double> data1=balanceData;
        singleLineManager.setCount(data1.size());
        singleLineManager.setLineName("资产");
        singleLineManager.setMonth(Calendar.getInstance());
        LineData linedata = singleLineManager.initSingleLineChart(ActivityBookkeepProperty.this,data1);
        singleLineManager.initDataStyle(mLine,linedata,ActivityBookkeepProperty.this);
    }
}



