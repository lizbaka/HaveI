package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ActivityLongTermAnalyze extends BaseActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityLongTermAnalyze.class);
        context.startActivity(intent);
    }

    LinearLayout habitTimesLL;
    TextView habitTimesTV;
    LinearLayout bestHabitLL;
    TextView bestHabitTV;
    LinearLayout habit21LL;
    TextView habit21TV;
    LinearLayout habit212LL;
    LinearLayout bookkeepCountLL;
    TextView bookkeepCountTV;
    LinearLayout mostExBookTagLL;
    TextView mostExBookTagTV;
    LinearLayout mostInBookTagLL;
    TextView mostInBookTagTV;
    LinearLayout mostSingleExLL;
    TextView mostSingleExBookTagTV;
    TextView mostSingleExValueTagTV;

    EventDBHelper eventDBHelper;
    BookkeepDBHelper bookkeepDBHelper;
    SharedPreferences pref;
    Calendar today;
    Calendar firstRun;
    BarChart Bar_event;
    BarChart Bar_book;

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

        configStatisticsCard();

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

    private void configStatisticsCard() {
        int habitTimes = eventDBHelper.getHabitExecCount();
        habitTimesTV.setText(Integer.toString(habitTimes));
        if (habitTimes <= 0) {
            bestHabitLL.setVisibility(View.GONE);
            habit21LL.setVisibility(View.GONE);
            habit212LL.setVisibility(View.GONE);
        } else {
            bestHabitLL.setVisibility(View.VISIBLE);
            habit21LL.setVisibility(View.VISIBLE);
            habit212LL.setVisibility(View.VISIBLE);
            bestHabitTV.setText(eventDBHelper.findAllHabitOrderByRank().get(0).getName());
            if (eventDBHelper.getHabit21Count() <= 0) {
                habit212LL.setVisibility(View.GONE);
            } else {
                habit212LL.setVisibility(View.VISIBLE);
            }
            habit21TV.setText(Integer.toString(eventDBHelper.getHabit21Count()));
        }
        int bookkeepCount = bookkeepDBHelper.findAllBookkeep().size();
        if (bookkeepCount <= 0) {
            mostInBookTagLL.setVisibility(View.GONE);
            mostExBookTagLL.setVisibility(View.GONE);
            mostSingleExLL.setVisibility(View.GONE);
        } else {
            mostInBookTagLL.setVisibility(View.VISIBLE);
            mostExBookTagLL.setVisibility(View.VISIBLE);
            mostSingleExLL.setVisibility(View.VISIBLE);
            bookkeepCountTV.setText(Integer.toString(bookkeepCount));
            List<BookTag> bookTag = bookkeepDBHelper.findAllBookTagSortByPM(UniToolKit.BOOKKEEP_TAG_INCOME);
            if (bookTag.isEmpty()) {
                mostInBookTagLL.setVisibility(View.GONE);
            } else {
                mostInBookTagLL.setVisibility(View.VISIBLE);
                mostInBookTagTV.setText(bookTag.get(0).getName());
            }
            bookTag = bookkeepDBHelper.findAllBookTagSortByPM(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
            if (bookTag.isEmpty()) {
                mostExBookTagLL.setVisibility(View.GONE);
            } else {
                mostExBookTagLL.setVisibility(View.VISIBLE);
                mostExBookTagTV.setText(bookTag.get(0).getName());
            }
            Bookkeep mostExBookkeep = bookkeepDBHelper.getMostSingleEx();
            if (mostExBookkeep == null) {
                mostSingleExLL.setVisibility(View.GONE);
            } else {
                mostSingleExLL.setVisibility(View.VISIBLE);
                mostSingleExBookTagTV.setText(bookkeepDBHelper.findBookTagById(mostExBookkeep.gettag()).getName());
                mostSingleExValueTagTV.setText(String.format("%.2f", Math.abs(mostExBookkeep.getPM())));
            }
        }
        setBookkeepBAR(Bar_book);
        setEventBAR(Bar_event);
    }

    private void initView() {
        Bar_event = findViewById(R.id.barChart_event);
        Bar_book = findViewById(R.id.barChart_book);
        habitTimesLL = findViewById(R.id.long_term_habit_times_container);
        habitTimesTV = findViewById(R.id.long_term_habit_times);
        bestHabitLL = findViewById(R.id.long_term_best_habit_container);
        bestHabitTV = findViewById(R.id.long_term_best_habit);
        habit21LL = findViewById(R.id.long_term_habit_21_container);
        habit21TV = findViewById(R.id.long_term_habit_21);
        habit212LL = findViewById(R.id.long_term_habit_21_container2);
        bookkeepCountLL = findViewById(R.id.long_term_bookkeep_count_container);
        bookkeepCountTV = findViewById(R.id.long_term_bookkeep_count);
        mostExBookTagLL = findViewById(R.id.long_term_bookkeep_expenditure_most_tag_container);
        mostExBookTagTV = findViewById(R.id.long_term_bookkeep_expenditure_most_tag);
        mostInBookTagLL = findViewById(R.id.long_term_bookkeep_income_most_tag_container);
        mostInBookTagTV = findViewById(R.id.long_term_bookkeep_income_most_tag);
        mostSingleExLL = findViewById(R.id.long_term_bookkeep_expenditure_single_most_container);
        mostSingleExBookTagTV = findViewById(R.id.long_term_bookkeep_expenditure_single_most_tag);
        mostSingleExValueTagTV = findViewById(R.id.long_term_bookkeep_expenditure_single_most_value);

        ((TextView) findViewById(R.id.long_term_first_date)).setText(UniToolKit.eventDateFormatter(firstRun.getTime()));
        ((TextView) findViewById(R.id.long_term_days)).setText(Integer.toString(calcDayOffset(firstRun.getTime(), today.getTime())));

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

    public void setBookkeepBAR(BarChart mBar) {
        ArrayList<BarEntry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();
        HashMap<Long, Double> mHash = bookkeepDBHelper.getSurplusListMonthly(firstRun.getTime());

        for (Long datel : mHash.keySet()) {
            xValues.add(UniToolKit.eventYearMonthFormatter(new Date(datel)));
        }
        int i = 0;
        for (Double mMoney : mHash.values()) {
            yValues.add(new BarEntry(i, mMoney.floatValue()));
            i+=1;
        }


        //拖动效果预览
//        for(Integer  j=0;j<20;j++)
//        {
//            yValues.add(new BarEntry(j,j.floatValue() ));
//        }
//        for(Integer i=0;i<20;i++)
//        {
//            xValues.add(i.toString());
//
//        }


        BarDataSet barDataSet = new BarDataSet(yValues, "每月");
        barDataSet.setDrawValues(true);
        barDataSet.setColor(ActivityLongTermAnalyze.this.getResources().getColor(R.color.amber_700));
        BarData mdata = new BarData(barDataSet);
        barDataSet.setValueTextColor(Color.BLACK); //数值显示的颜色
        barDataSet.setValueTextSize(10f);//数值显示的大小
        barDataSet.setBarBorderWidth(10f);
        barDataSet.setColor(Color.rgb(255, 165, 0));
        barDataSet.setBarBorderColor(Color.WHITE);
        mBar.setData(mdata);
        mBar.getDescription().setEnabled(false);
        mBar.setDrawBorders(false);
        mBar.setGridBackgroundColor(Color.GRAY & 0x70FFFFFF);
        mBar.setTouchEnabled(true); //可点击
        mBar.setDragEnabled(true);  //可拖拽
        mBar.setScaleEnabled(false);  //可缩放
        mBar.setPinchZoom(false);
        mBar.setBackgroundColor(Color.WHITE);
        XAxis xAxis = mBar.getXAxis();  //x轴的标示
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String lab;
                int index = (int) value;
                Log.d(TAG, "getFormattedValue: " + value + "," + index);
                lab = xValues.get(index);
                return lab;
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.BLACK);    //字体的颜色
        xAxis.setTextSize(12f); //字体大小
        xAxis.setDrawGridLines(false); //不显示网格线
//        xAxis.setGridLineWidth(10f);
        xAxis.setGranularity(1f);


        YAxis axisLeft = mBar.getAxisLeft(); //y轴左边标示
        YAxis axisRight = mBar.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.GRAY); //字体颜色
        axisLeft.setTextSize(12f); //字体大小
//        axisLeft.setAxisMaxValue((float) max); //最大值
//        axisLeft.setAxisMinimum(0f);
        axisLeft.setLabelCount(5, true); //显示格数
        axisLeft.setGridColor(Color.GRAY); //网格线颜色
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

//        mBar.setVisibleXRange(0, 6);
        //设置动画效果
        mBar.animateY(1000, Easing.Linear);
        mBar.animateX(1000, Easing.Linear);
        mBar.invalidate();

    }

    public void setEventBAR(BarChart mBar) {
        ArrayList<BarEntry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();
        HashMap<Long, Integer> mHash = eventDBHelper.getHabitExecCountListMonthly(firstRun.getTime());

        for (Long datel : mHash.keySet()) {
            xValues.add(UniToolKit.eventYearMonthFormatter(new Date(datel)));
        }
        int i = 0;
        for (Integer mTimes : mHash.values()) {
            yValues.add(new BarEntry(i, mTimes.floatValue()));
            i++;
        }
        BarDataSet barDataSet = new BarDataSet(yValues, "每月");
        barDataSet.setBarBorderWidth(10f);
        barDataSet.setDrawValues(true);
        barDataSet.setColor(ActivityLongTermAnalyze.this.getResources().getColor(R.color.amber_700));
        BarData mdata = new BarData(barDataSet);
        barDataSet.setBarBorderWidth(10f);
        barDataSet.setBarBorderColor(Color.WHITE);
        barDataSet.setColor(Color.rgb(255, 165, 0));
        barDataSet.setValueTextColor(Color.BLACK); //数值显示的颜色
        barDataSet.setValueTextSize(10f);     //数值显示的大小
        mBar.setData(mdata);
        mBar.getDescription().setText("");
        mBar.setDrawBorders(false);
        mBar.setGridBackgroundColor(Color.GRAY & 0x70FFFFFF);
        mBar.setTouchEnabled(true); //可点击
        mBar.setDragEnabled(true);  //可拖拽
        mBar.setScaleEnabled(false);  //可缩放
        mBar.setPinchZoom(false);
        mBar.setBackgroundColor(Color.WHITE);
        XAxis xAxis = mBar.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.BLACK);    //字体的颜色
        xAxis.setTextSize(12f); //字体大小
//        mBar.setVisibleXRange(1, 6);
        xAxis.setDrawGridLines(false); //不显示网格线
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String lab;
                int index = (int) value;
                lab = xValues.get(index);
                return lab;
            }
        });
        xAxis.setGranularity(1f);
        YAxis axisLeft = mBar.getAxisLeft(); //y轴左边标示
        YAxis axisRight = mBar.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.GRAY); //字体颜色
        axisLeft.setTextSize(12f); //字体大小
//        axisLeft.setAxisMaxValue((float) max); //最大值
        axisLeft.setAxisMinimum(0f);
//        axisLeft.setLabelCount(5, true); //显示格数
        axisLeft.setGridColor(Color.GRAY); //网格线颜色
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);
        //设置动画效果
        mBar.animateY(1000, Easing.Linear);
        mBar.animateX(1000, Easing.Linear);
        mBar.invalidate();

    }
}