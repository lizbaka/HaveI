package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.Event.BookCou;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.DatePickerDialog;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.BookkeepCardAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityBookkeep extends BaseActivity {


    private MaterialCardView mShowDateBTN;
    private TextView mSelectDateTV;
    private String sDate;//只有月份
    private BookkeepDBHelper dbHelper;
    private BookCou mBookCou;
    private List<Bookkeep> mBookList;
    private String initTime;
    private Double MYin = 0d;
    private Double MYout = 0d;
    private Double MYleft = 0d;
    private TextView month_in;
    private TextView month_out;
    private TextView month_left;
    private RecyclerView mRec;
    private BookkeepCardAdapter mbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep);
        dbHelper = new BookkeepDBHelper(ActivityBookkeep.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        init();
        mShowDateBTN.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityBookkeep.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        String textString = String.format("%d-%02d", startYear,
                                startMonthOfYear + 1);
                        mSelectDateTV.setText(textString);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
                sDate = mSelectDateTV.getText().toString();
                update();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickFindAnnualAccountDetail(View view) {
        Intent intent = new Intent(this, ActivityBookkeepAnnualAccountDetail.class);
        startActivity(intent);
    }

    public void onClickFindStatisticsView(View view) {
//        Intent intent = new Intent(this, ActivityBookkeepStatisticsView.class);
//        startActivity(intent);
    }

    public void onClickFindPropertyView(View view) {
        Intent intent = new Intent(this, ActivityBookkeepPropertyView.class);
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    void init() {
        setSupportActionBar(findViewById(R.id.bookkeep_toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mShowDateBTN = findViewById(R.id.btn_show_date);
        mSelectDateTV = findViewById(R.id.tv_select_date);
        month_in = findViewById(R.id.bookkeep_window_income_value);
        month_out = findViewById(R.id.bookkeep_window_expenditure_value);
        month_left = findViewById(R.id.bookkeep_window_remaining_budget_value);
        mRec = findViewById(R.id.recyclerView_today_detail);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
        sDate = sDateFormat.format(new java.util.Date());
        mSelectDateTV.setText(sDate);
        mBookCou = dbHelper.findBookcouByMonth(sDate);
        if (mBookCou == null) {
            updateCard(0, 0);
            updateRec(false);
        } else {
            updateCard(mBookCou.getIn(), mBookCou.getOut());
            updateRec(true);
        }


    }

    @SuppressLint("SetTextI18n")
    void updateCard(double in, double out) {
        MYin = in;
        MYleft = in - out;
        MYout = out;
//        month_in.setText(MYin.toString());
//        month_out.setText(MYout.toString());
//        month_left.setText(MYleft.toString());
        month_in.setText(String.format("%.2f", MYin));
        month_out.setText(String.format("%.2f", MYout));
        month_left.setText(String.format("%.2f", MYleft));
    }

    void updateRec(boolean is) {
        if (is) {
            mBookList = dbHelper.findBookkeepByMonth(sDate);
        } else {
            mBookList = new ArrayList<>();
        }
        mbAdapter = new BookkeepCardAdapter(mBookList, ActivityBookkeep.this);
        mRec.setLayoutManager(new LinearLayoutManager(ActivityBookkeep.this));
        mRec.setAdapter(mbAdapter);

    }

    void update() {
        mBookCou = dbHelper.findBookcouByMonth(sDate);
        if (mBookCou == null) {
            updateCard(0, 0);
            updateRec(false);
        } else {
            updateCard(mBookCou.getIn(), mBookCou.getOut());
            updateRec(true);
        }
    }


    public void onClickAddBookkeep(View view) {
        Intent intent = new Intent(this, ActivityBookkeepAdd.class);
        startActivity(intent);

    }
}
