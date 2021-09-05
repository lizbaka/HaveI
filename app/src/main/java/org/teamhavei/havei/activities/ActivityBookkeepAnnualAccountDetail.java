package org.teamhavei.havei.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.teamhavei.havei.Event.BookCou;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.Bookran;
import org.teamhavei.havei.Event.doubleLineManager;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ActivityBookkeepAnnualAccountDetail extends AppCompatActivity {
    private Button mShowDateBTN;
    private TextView mSelectDateTV;
    private String sDate;
    private BookkeepDBHelper dbHelper= new BookkeepDBHelper(ActivityBookkeepAnnualAccountDetail.this,BookkeepDBHelper.DB_NAME,null, BookkeepDBHelper.DATABASE_VERSION);
    private BookCou mBookCou;
    private List<Bookkeep> mBookList;
    private String initTime;
    private PieChart chart_in;
    private PieChart chart_out;
    private ArrayList<Bookran> rans_in;
    private ArrayList<Bookran> rans_out;
    private ArrayList<BookCou> cou_line;
    private int numOfran;
    private int numOfcou;
    private LineChart Line_one;
    private LineData linedata1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_annual_account_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_bookkeep_annual_account_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();

    }


    void init()
    {
        int i;
        chart_in=findViewById(R.id.chart_in);
        chart_out=findViewById(R.id.chart_out);
        Line_one=findViewById(R.id.chart_line1);
        SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy");
        sDate=sDateFormat.format(new java.util.Date());
        rans_in=dbHelper.Getran(true,sDate);
        rans_out=dbHelper.Getran(false,sDate);
        setchartPIE(rans_in,chart_in,"收入");
        setchartPIE(rans_out,chart_out,"支出");



        cou_line=dbHelper.findBookCoubyYear(sDate);
        numOfcou=cou_line.size();
        ArrayList<Double> data1=new ArrayList<>();
        for(i=0;i<numOfcou;i++)
            data1.add(cou_line.get(i).getIn());
        ArrayList<Double> data2=new ArrayList<>();
        for(i=0;i<numOfcou;i++)
            data2.add(cou_line.get(i).getOut());
        doubleLineManager.setCount(numOfcou);
        doubleLineManager.setLineName("收入");
        doubleLineManager.setLineName1("支出");
        linedata1= doubleLineManager.initDoubleLineChart(ActivityBookkeepAnnualAccountDetail.this,data1,data2);
        doubleLineManager.initDataStyle(Line_one,linedata1,ActivityBookkeepAnnualAccountDetail.this);


    }
    public void setchartPIE(ArrayList<Bookran> mran,PieChart mchart,String name)
    {
        int i=0;
        numOfran =mran.size();
        HashMap dataMap =new HashMap();
        for(i=0; i< numOfran; i++)
        {
            dataMap.put(mran.get(i).name,mran.get(i).counts);
        }

        setPieChart(mchart,dataMap,name,true);
    }
    public static final int[] PIE_COLORS = {
            Color.rgb(	72,209,204),
            Color.rgb(	60,179,113),
            Color.rgb(135,206,250), Color.rgb(	135,206,235), Color.rgb(241, 214, 145),
            Color.rgb(0,0,205)
    };


    //
    public void setPieChart(PieChart pieChart, Map<String, Float> pieValues, String title, boolean showLegend) {
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
        legend.setEnabled(false);
//        if (showLegend) {
//            legend.setEnabled(true);//是否显示图例
//            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例相对于图表横向的位置
//            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例相对于图表纵向的位置
//            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例显示的方向
//            legend.setDrawInside(false);
//            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        } else {
//            legend.setEnabled(false);
//        }

        //设置饼图数据
        setPieChartData(pieChart, pieValues);

        pieChart.animateX(1500, Easing.EaseInOutQuad);//数据显示动画

    }

    //-->设置饼图数据
    private void setPieChartData(PieChart pieChart, Map<String, Float> pieValues) {

        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        ArrayList<PieEntry> entries=new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
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