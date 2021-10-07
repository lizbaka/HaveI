package org.teamhavei.havei.activities;
// TODO: 2021/10/3 完成图表构建

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.Event.HaveIDatePickerDialog;
import org.teamhavei.havei.Event.doubleLineManager;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;



public class ActivityBookkeepStatisticAnnually extends AppCompatActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBookkeepStatisticAnnually.class);
        context.startActivity(intent);
    }

    BookkeepDBHelper dbHelper;
    Calendar calendar;

    TextView dateTV;
    TextView incomeTV;
    TextView expenditureTV;
    TextView surplusTV;
    ExtendedFloatingActionButton switchFAB;
    private LineChart Line_one;
    private LineData linedata1;
    BarChart bar_one;
    double[] expenditureByMonth = new double[12];
    double[] incomeByMonth = new double[12];
    private static ArrayList<String> Month=new ArrayList<String>(Arrays.asList("1","一","二","三","四","五","六","七","八","九","十","十一","十二"));

    private Date nowDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_statistic_annually);
        setSupportActionBar(findViewById(R.id.bookkeep_statistics_annually_toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        dbHelper = new BookkeepDBHelper(ActivityBookkeepStatisticAnnually.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        calendar = Calendar.getInstance();
        setmyDOULINE(calendar.getTime(),Line_one);
        setmyBAR(calendar.getTime(),bar_one);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void updateData() {
        dateTV.setText(Integer.toString(calendar.get(Calendar.YEAR)) + getString(R.string.bookkeep_annual_statistic));
        double totalExpenditure = 0;
        double totalIncome = 0;
        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, i);
            expenditureByMonth[i] = dbHelper.getExpenditureByMonth(calendar.getTime());
            totalExpenditure += expenditureByMonth[i];
            incomeByMonth[i] = dbHelper.getIncomeByMonth(calendar.getTime());
            totalIncome += incomeByMonth[i];
        }
        expenditureTV.setText(String.format("%.2f", totalExpenditure));
        incomeTV.setText(String.format("%.2f", totalIncome));
        surplusTV.setText(String.format("%.2f", totalIncome - totalExpenditure));
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

    void initView() {
        dateTV = findViewById(R.id.bookkeep_statistics_annually_date);
        expenditureTV = findViewById(R.id.bookkeep_three_value1);
        incomeTV = findViewById(R.id.bookkeep_three_value2);
        surplusTV = findViewById(R.id.bookkeep_three_value3);
        switchFAB = findViewById(R.id.bookkeep_annual_switch_fab);
        Line_one=findViewById(R.id.chart_line1);
        bar_one=findViewById(R.id.barChart);



        ((TextView) findViewById(R.id.bookkeep_three_title1)).setText(R.string.bookkeep_annual_expenditure);
        ((TextView) findViewById(R.id.bookkeep_three_title2)).setText(R.string.bookkeep_annual_income);
        ((TextView) findViewById(R.id.bookkeep_three_title3)).setText(R.string.bookkeep_annual_surplus);

        findViewById(R.id.bookkeep_statistics_annually_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HaveIDatePickerDialog(ActivityBookkeepStatisticAnnually.this, 0, new HaveIDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                        calendar.set(Calendar.YEAR, startYear);
                        updateData();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .hideDay(true)
                        .hideMonth(true)
                        .setMaxDate(Calendar.getInstance())
                        .show();
            }
        });

        switchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityBookkeepStatisticMonthly.startAction(ActivityBookkeepStatisticAnnually.this);
                finish();
            }
        });

        ((NestedScrollView)findViewById(R.id.bookkeep_statistic_annually_scroll_view)).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>oldScrollY){
                    switchFAB.hide();
                }else{
                    switchFAB.show();
                }
            }
        });
    }
    public void setmyDOULINE(Date mDate,LineChart mLine)

    {
        ArrayList<Double> data1=dbHelper.getPMListByYear(UniToolKit.BOOKKEEP_TAG_INCOME,mDate);
        ArrayList<Double> data2=dbHelper.getPMListByYear(UniToolKit.BOOKKEEP_TAG_EXPENDITURE,mDate);
        doubleLineManager.setCount(data1.size());
        doubleLineManager.setLineName("收入");
        doubleLineManager.setLineName1("支出");
        LineData linedata = doubleLineManager.initDoubleLineChart(ActivityBookkeepStatisticAnnually.this,data1,data2);
        doubleLineManager.initDataStyle(mLine,linedata,ActivityBookkeepStatisticAnnually.this);


    }
    public void setmyBAR(Date mdate, BarChart mBar)
    {
        ArrayList<BarEntry> yValues= new ArrayList<>();
        ArrayList<Double> suplist= dbHelper.getSurplusListByYear(mdate);
        for(int i =0;i<suplist.size();i++)
        {
            yValues.add(new BarEntry(i+1,suplist.get(i).floatValue()));
        }
        BarDataSet barDataSet=new BarDataSet(yValues,"盈余");
        barDataSet.setDrawValues(true);
        barDataSet.setColor(ActivityBookkeepStatisticAnnually.this.getResources().getColor(R.color.amber_700));
        BarData mdata=new BarData(barDataSet);
        barDataSet.setValueTextColor(Color.BLACK); //数值显示的颜色
        barDataSet.setValueTextSize(10f);     //数值显示的大小
        mBar.setData(mdata);
        Description description=new Description();
        description.setText("快康康，还吃嗄");
        description.setTextAlign(Paint.Align.CENTER);
        description.setTextSize(10);
        description.setPosition(200, 150);
        mBar.setDescription(description);
        mBar.setDrawBorders(false);
        mBar.setGridBackgroundColor(Color.GRAY & 0x70FFFFFF);
       mBar.setTouchEnabled(true); //可点击
        mBar.setDragEnabled(false);  //可拖拽
        mBar.setScaleEnabled(false);  //可缩放
        mBar.setPinchZoom(false);
        mBar.setBackgroundColor(Color.WHITE);
        XAxis xAxis = mBar.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.BLACK);    //字体的颜色
        xAxis.setTextSize(10f); //字体大小
        xAxis.setDrawGridLines(false); //不显示网格线
        xAxis.setValueFormatter(new IndexAxisValueFormatter()
        { @Override
        public String getFormattedValue(float value) {
            String lab;
            int index=(int)value;
            lab=Month.get(index);
            return lab;
        }
        });
        xAxis.setGranularity(0.9f);
        YAxis axisLeft = mBar.getAxisLeft(); //y轴左边标示
        YAxis axisRight = mBar.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.GRAY); //字体颜色
        axisLeft.setTextSize(10f); //字体大小
//        axisLeft.setAxisMaxValue((float) max); //最大值
//        axisLeft.setAxisMinimum(0f);
//        axisLeft.setLabelCount(5, true); //显示格数
        axisLeft.setGridColor(Color.GRAY); //网格线颜色
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);
        //设置动画效果
        mBar.animateY(2000, Easing.Linear);
        mBar.animateX(2000, Easing.Linear);
        mBar.invalidate();

    }
}