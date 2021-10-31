package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.event.Bookkeep;
import org.teamhavei.havei.event.HaveIDatePickerDialog;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.BookkeepCardAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.widgets.WidgetBookkeepOverview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityBookkeep extends BaseActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBookkeep.class);
        context.startActivity(intent);
    }

    SharedPreferences pref;
    double budget;
    Calendar calendar;

    MaterialCardView mShowDateBTN;
    TextView mSelectDateTV;
    BookkeepDBHelper dbHelper;
    List<Bookkeep> mBookList;
    TextView month_in;
    TextView month_out;
    TextView month_left;
    RecyclerView recordRV;
    ExtendedFloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep);
        setSupportActionBar(findViewById(R.id.bookkeep_toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new BookkeepDBHelper(ActivityBookkeep.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        pref = getSharedPreferences(UniToolKit.PREF_SETTINGS, MODE_PRIVATE);
        init();

        mShowDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HaveIDatePickerDialog(ActivityBookkeep.this, 0, new HaveIDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        calendar.set(startYear, startMonthOfYear, startDayOfMonth);
                        mSelectDateTV.setText(UniToolKit.eventYearMonthFormatter(calendar.getTime()));
                        update();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .setMaxDate(Calendar.getInstance())
                        .hideDay(true)
                        .show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_2_0://property
                ActivityBookkeepProperty.startAction(ActivityBookkeep.this);
                return true;
            case R.id.toolbar_2_1://statistic
                ActivityBookkeepStatisticMonthly.startAction(ActivityBookkeep.this);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_2,menu);
        menu.getItem(0).setTitle(R.string.property).setIcon(R.drawable.ic_baseline_timeline_24);
        menu.getItem(1).setTitle(R.string.analyze).setIcon(R.drawable.ic_baseline_leaderboard_24);
        return super.onCreateOptionsMenu(menu);
    }

    void init() {
        calendar = Calendar.getInstance();

        mShowDateBTN = findViewById(R.id.btn_show_date);
        mSelectDateTV = findViewById(R.id.tv_select_date);
        month_in = findViewById(R.id.bookkeep_three_value3);
        month_out = findViewById(R.id.bookkeep_three_value1);
        month_left = findViewById(R.id.bookkeep_three_value2);
        recordRV = findViewById(R.id.recyclerView_today_detail);
        mSelectDateTV.setText(UniToolKit.eventYearMonthFormatter(calendar.getTime()));
        fab = findViewById(R.id.bookkeep_add);

        ((TextView) findViewById(R.id.bookkeep_three_title3)).setText(R.string.income);
        ((TextView) findViewById(R.id.bookkeep_three_title1)).setText(R.string.expenditure);
        ((TextView) findViewById(R.id.bookkeep_three_title2)).setText(R.string.remaining_budget);

        mBookList = new ArrayList<>();
        recordRV.setLayoutManager(new LinearLayoutManager(ActivityBookkeep.this));
        recordRV.setAdapter(new BookkeepCardAdapter(mBookList, ActivityBookkeep.this, new BookkeepCardAdapter.BookkeepCardCallBack() {
            @Override
            public void onLongClick(Bookkeep bookkeep) {
                new AlertDialog.Builder(ActivityBookkeep.this)
                        .setCancelable(true)
                        .setTitle(R.string.bookkeep_delete_dialog_title)
                        .setMessage(R.string.bookkeep_delete_dialog_msg)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteBookkeep(bookkeep);
                                update();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

            @Override
            public void onClick(Bookkeep bookkeep) {
                ActivityBookkeepAdd.startAction(ActivityBookkeep.this, bookkeep.getid());
            }
        }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityBookkeepAdd.startAction(ActivityBookkeep.this);
            }
        });

        recordRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

        findViewById(R.id.bookkeep_overview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityBudget.startAction(ActivityBookkeep.this);
            }
        });


    }

    @SuppressLint({"DefaultLocale"})
    void updateCard(double in, double out) {
        Double MYin = in;
        Double MYleft = budget - out;
        Double MYout = out;
        month_in.setText(String.format("%.2f", MYin));
        month_out.setText(String.format("%.2f", MYout));
        if (budget == 0) {
            month_left.setText(R.string.unset);
        } else {
            month_left.setText(String.format("%.2f", MYleft));
        }
    }

    private void updateRecord() {
        mBookList.clear();
        mBookList.addAll(dbHelper.findBookkeepByMonth(UniToolKit.eventYearMonthFormatter(calendar.getTime())));
        if(mBookList.isEmpty()){
            findViewById(R.id.bookkeep_empty).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.bookkeep_empty).setVisibility(View.GONE);
        }
        ((BookkeepCardAdapter) recordRV.getAdapter()).notifyBookListChanged();
    }

    private void update() {
        WidgetBookkeepOverview.updateWidgetAction(ActivityBookkeep.this);
        budget = pref.getFloat(UniToolKit.PREF_BUDGET, 0);
        updateRecord();
        updateCard(dbHelper.getIncomeByMonth(calendar.getTime()), dbHelper.getExpenditureByMonth(calendar.getTime()));
    }
}
