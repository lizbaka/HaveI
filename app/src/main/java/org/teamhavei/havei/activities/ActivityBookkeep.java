package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.HaveIDatePickerDialog;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.BookkeepCardAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityBookkeep extends BaseActivity {

    SharedPreferences pref;
    double budget;
    Calendar calendar;

    private MaterialCardView mShowDateBTN;
    private TextView mSelectDateTV;
    private BookkeepDBHelper dbHelper;
//    private BookCou mBookCou;
    private List<Bookkeep> mBookList;
    private Double MYin = 0d;
    private Double MYout = 0d;
    private Double MYleft = 0d;
    private TextView month_in;
    private TextView month_out;
    private TextView month_left;
    private RecyclerView recordRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep);
        setSupportActionBar(findViewById(R.id.bookkeep_toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new BookkeepDBHelper(ActivityBookkeep.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
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
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pref = getSharedPreferences(UniToolKit.PREF_SETTINGS, MODE_PRIVATE);
        budget = pref.getFloat(UniToolKit.PREF_BUDGET, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickFindAnnualAccountDetail(View view) {
//        Intent intent = new Intent(this, ActivityBookkeepAnnualAccountDetail.class);
//        startActivity(intent);
    }

    public void onClickFindStatisticsView(View view) {
//        Intent intent = new Intent(this, ActivityBookkeepStatisticsView.class);
//        startActivity(intent);
    }

    public void onClickFindPropertyView(View view) {
//        Intent intent = new Intent(this, ActivityBookkeepPropertyView.class);
//        startActivity(intent);
    }

    public void onClickAddBookkeep(View view) {
        ActivityBookkeepAdd.startAction(ActivityBookkeep.this);
    }


    void init() {
        mShowDateBTN = findViewById(R.id.btn_show_date);
        mSelectDateTV = findViewById(R.id.tv_select_date);
        month_in = findViewById(R.id.bookkeep_window_income_value);
        month_out = findViewById(R.id.bookkeep_window_expenditure_value);
        month_left = findViewById(R.id.bookkeep_window_remaining_budget_value);
        recordRV = findViewById(R.id.recyclerView_today_detail);
        calendar = Calendar.getInstance();
        mSelectDateTV.setText(UniToolKit.eventYearMonthFormatter(calendar.getTime()));

        mBookList = new ArrayList<>();
        recordRV.setLayoutManager(new LinearLayoutManager(ActivityBookkeep.this));
        recordRV.setAdapter(new BookkeepCardAdapter(mBookList, ActivityBookkeep.this));
        recordRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    findViewById(R.id.bookkeep_bottom_bar).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.bookkeep_bottom_bar).setVisibility(View.VISIBLE);
                }
            }
        });

//        mBookCou = dbHelper.findBookcouByMonth(sDate);
//        if (mBookCou == null) {
//            updateCard(0, 0);
//            updateRec(false);
//        } else {
//            updateCard(mBookCou.getIn(), mBookCou.getOut());
//            updateRec(true);
//        }


    }

    @SuppressLint("SetTextI18n")
    void updateCard(double in, double out) {
        MYin = in;
        MYleft = budget - out;
        MYout = out;
//        month_in.setText(MYin.toString());
//        month_out.setText(MYout.toString());
//        month_left.setText(MYleft.toString());
        month_in.setText(String.format("%.2f", MYin));
        month_out.setText(String.format("%.2f", MYout));
        if (budget == 0) {
            month_left.setText(R.string.unset);
        } else {
            month_left.setText(String.format("%.2f", MYleft));
        }
    }

    private void updateRecord(boolean is) {
//        if (is) {
//            mBookList = dbHelper.findBookkeepByMonth(sDate);
//        } else {
//            mBookList = new ArrayList<>();
//        }
        mBookList.clear();
        mBookList.addAll(dbHelper.findBookkeepByMonth(UniToolKit.eventYearMonthFormatter(calendar.getTime())));
        ((BookkeepCardAdapter) recordRV.getAdapter()).notifyBookListChanged();
    }

    private void update() {
//        mBookCou = dbHelper.findBookcouByMonth(sDate);
//        if (mBookCou == null) {
//            updateCard(0, 0);
//            updateRec(false);
//        } else {
//            updateCard(mBookCou.getIn(), mBookCou.getOut());
//            updateRec(true);
//        }
        updateRecord(true);
        updateCard(dbHelper.getIncomeByMonth(calendar.getTime()), dbHelper.getExpenditureByMonth(calendar.getTime()));
    }

}
