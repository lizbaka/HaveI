package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.Event.HaveIDatePickerDialog;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.util.Calendar;

public class ActivityBookkeepStatisticMonthly extends BaseActivity {

    BookkeepDBHelper dbHelper;
    MaterialCardView dateSelectorMCV;
    TextView dateTV;
    TextView incomeTV;
    TextView expenditureTV;
    TextView surplusTV;

    Calendar calendar;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBookkeepStatisticMonthly.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_statistic_monthly);

        dbHelper = new BookkeepDBHelper(ActivityBookkeepStatisticMonthly.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        calendar = Calendar.getInstance();

        setSupportActionBar(findViewById(R.id.bookkeep_statistic_monthly_toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        dateSelectorMCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HaveIDatePickerDialog(ActivityBookkeepStatisticMonthly.this, 0, new HaveIDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        calendar.set(startYear, startMonthOfYear, startDayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .setMaxDate(Calendar.getInstance())
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateTV.setText(UniToolKit.eventYearMonthFormatter(calendar.getTime()));
        updateData();
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

    private void initView(){
        dateSelectorMCV = findViewById(R.id.bookkeep_statistic_monthly_month_selector);
        dateTV = findViewById(R.id.bookkeep_statistic_monthly_month_date);
        expenditureTV = findViewById(R.id.bookkeep_three_value1);
        incomeTV = findViewById(R.id.bookkeep_three_value2);
        surplusTV = findViewById(R.id.bookkeep_three_value3);

        ((TextView)findViewById(R.id.bookkeep_three_title1)).setText(R.string.expenditure);
        ((TextView)findViewById(R.id.bookkeep_three_title2)).setText(R.string.income);
        ((TextView)findViewById(R.id.bookkeep_three_title3)).setText(R.string.surplus);
    }

    @SuppressLint({"DefaultLocale"})
    private void updateData(){
        double income = dbHelper.getIncomeByMonth(calendar.getTime());
        double expenditure = dbHelper.getExpenditureByMonth(calendar.getTime());
        double surplus = income - expenditure;
        incomeTV.setText(String.format("%.2f",income));
        expenditureTV.setText(String.format("%.2f",expenditure));
        surplusTV.setText(String.format("%.2f",surplus));
    }
}