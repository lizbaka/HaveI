package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.Calendar;
import java.util.Date;

public class ActivityLongTermAnalyze extends BaseActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityLongTermAnalyze.class);
        context.startActivity(intent);
    }

    TextView daysTV;

    EventDBHelper eventDBHelper;
    BookkeepDBHelper bookkeepDBHelper;
    SharedPreferences pref;
    Calendar today;
    Calendar firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_term_analyze);
        setSupportActionBar(findViewById(R.id.long_term_analyze_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventDBHelper = new EventDBHelper(ActivityLongTermAnalyze.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        bookkeepDBHelper = new BookkeepDBHelper(ActivityLongTermAnalyze.this, bookkeepDBHelper.DB_NAME, null, bookkeepDBHelper.DATABASE_VERSION);
        today = Calendar.getInstance();
        firstRun = Calendar.getInstance();

        pref = getSharedPreferences(UniToolKit.PREF_SETTINGS, MODE_PRIVATE);
        firstRun.setTimeInMillis(pref.getLong(UniToolKit.PREF_FIRST_RUN_DATE, firstRun.getTimeInMillis()));

        initView();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){


        ((TextView)findViewById(R.id.long_term_first_date)).setText(UniToolKit.eventDateFormatter(firstRun.getTime()));
        ((TextView)findViewById(R.id.long_term_days)).setText(Integer.toString(calcDayOffset(firstRun.getTime(), today.getTime())));
    }

    private void analyzeForSuggest(){

    }

    public static int calcDayOffset(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {  //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                    timeDistance += 366;
                } else {  //不是闰年

                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { //不同年
            return day2 - day1;
        }
    }
}