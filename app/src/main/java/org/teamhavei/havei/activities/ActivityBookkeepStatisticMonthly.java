package org.teamhavei.havei.activities;
// TODO: 2021/10/3 加入图表

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.Event.HaveIDatePickerDialog;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ActivityBookkeepStatisticMonthly extends BaseActivity {

    BookkeepDBHelper dbHelper;
    MaterialCardView dateSelectorMCV;
    TextView dateTV;
    TextView incomeTV;
    TextView expenditureTV;
    TextView surplusTV;
    ExtendedFloatingActionButton switchFAB;

    Calendar calendar;
    PieChart chart_in;
    PieChart chart_out;

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
        //饼状图

        setmyPIE(chart_in,"收入",UniToolKit.BOOKKEEP_TAG_INCOME,calendar.getTime());
        setmyPIE(chart_out,"支出",UniToolKit.BOOKKEEP_TAG_EXPENDITURE,calendar.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        switchFAB = findViewById(R.id.bookkeep_monthly_switch_fab);
         chart_in= findViewById(R.id.chart_in);
       chart_out=findViewById(R.id.chart_out);

        dateSelectorMCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HaveIDatePickerDialog(ActivityBookkeepStatisticMonthly.this, 0, new HaveIDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        calendar.set(startYear, startMonthOfYear, startDayOfMonth);
                        updateData();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .setMaxDate(Calendar.getInstance())
                        .hideDay(true)
                        .show();
            }
        });

        switchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityBookkeepStatisticAnnually.startAction(ActivityBookkeepStatisticMonthly.this);
                finish();
            }
        });

        ((NestedScrollView)findViewById(R.id.bookkeep_statistic_monthly_scroll_view)).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>oldScrollY){
                    switchFAB.hide();
                }else{
                    switchFAB.show();
                }
            }
        });

        ((TextView)findViewById(R.id.bookkeep_three_title1)).setText(R.string.expenditure);
        ((TextView)findViewById(R.id.bookkeep_three_title2)).setText(R.string.income);
        ((TextView)findViewById(R.id.bookkeep_three_title3)).setText(R.string.surplus);
    }

    @SuppressLint({"DefaultLocale"})
    private void updateData(){
        dateTV.setText(UniToolKit.eventYearMonthFormatter(calendar.getTime()));
        double income = dbHelper.getIncomeByMonth(calendar.getTime());
        double expenditure = dbHelper.getExpenditureByMonth(calendar.getTime());
        double surplus = income - expenditure;
        incomeTV.setText(String.format("%.2f",income));
        expenditureTV.setText(String.format("%.2f",expenditure));
        surplusTV.setText(String.format("%.2f",surplus));
    }


    public void setmyPIE(PieChart mchart, String name, int type, Date mDate)
    {
        HashMap dataMap =dbHelper.getTagDataByMonth(type,mDate);
        setPieChart(mchart,dataMap,name,true);
    }
    public static final int[] PIE_COLORS = {
            Color.rgb(	72,209,204),
            Color.rgb(	60,179,113),
            Color.rgb(135,206,250), Color.rgb(	135,206,235), Color.rgb(241, 214, 145),
            Color.rgb(0,0,205),Color.rgb(1,2,3),Color.rgb(4,3,4),Color.rgb(4,5,6)
    };
    //
    public void setPieChart(PieChart pieChart, HashMap<String, Double> pieValues, String title, boolean showLegend) {
        pieChart.setUsePercentValues(true);//设置使用百分比（后续有详细介绍）
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setExtraOffsets(25, 10, 25, 25); //设置边距
        pieChart.setDragDecelerationFrictionCoef(0.95f);//设置摩擦系数（值越小摩擦系数越大）
        pieChart.setCenterText(title);//设置环中的文字
        pieChart.setRotationEnabled(true);//是否可以旋转
        pieChart.setHighlightPerTapEnabled(true);//点击是否放大
        pieChart.setCenterTextSize(22f);//设置环中文字的大小
        pieChart.setDrawCenterText(true);//设置绘制环中文字
        pieChart.setRotationAngle(120f);//设置旋转角度
        pieChart.setTransparentCircleRadius(61f);//设置半透明圆环的半径,看着就有一种立体的感觉
        //这个方法为true就是环形图，为false就是饼图
        pieChart.setDrawHoleEnabled(true);
        //设置环形中间空白颜色是白色
        pieChart.setHoleColor(Color.WHITE);
        //设置半透明圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆环的透明度
        pieChart.setTransparentCircleAlpha(110);

        //图例设置
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        if (showLegend) {
            legend.setEnabled(true);//是否显示图例
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);//图例相对于图表横向的位置
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);//图例相对于图表纵向的位置
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);//图例显示的方向
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        } else {
            legend.setEnabled(false);
        }
        //设置饼图数据
        setPieChartData(pieChart, pieValues);

        pieChart.animateX(1500, Easing.EaseInOutQuad);//数据显示动画

    }

    //-->设置饼图数据
    private void setPieChartData(PieChart pieChart, HashMap<String, Double> pieValues) {

        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        ArrayList<PieEntry> entries=new ArrayList<>();
        while (it.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) it.next();
            entries.add(new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(5f);//设置饼块选中时偏离饼图中心的距离
        dataSet.setColors(PIE_COLORS);//设置饼块的颜色
        //设置数据显示方式有见图
        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.YELLOW);//设置连接线的颜色
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);
        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}