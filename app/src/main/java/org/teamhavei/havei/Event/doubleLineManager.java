package org.teamhavei.havei.Event;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;

public class doubleLineManager {

    private static String lineName = null;
    private static String lineName1 = null;
    private static int count = 0;
    private static ArrayList<String> Month=new ArrayList<String>(Arrays.asList("1","一","二","三","四","五","六","七","八","九","十","十一","十二",""));
    private static double max=600;
    public static LineData initDoubleLineChart(Context context, ArrayList<Double> datas1,ArrayList<Double> datas2) {
        // y轴的数据
        ArrayList<Entry> yValues1 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            yValues1.add(new Entry(i+1,datas1.get(i).floatValue()));

        }
        // y轴的数据
        ArrayList<Entry> yValues2 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            yValues2.add(new Entry(i+1,datas2.get(i).floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(yValues1, lineName);
        dataSet.setLineWidth(3.5f); // 线宽
        dataSet.setCircleSize(2f);// 显示的圆形大小
        dataSet.setColor(Color.rgb(89, 194, 230));// 折线显示颜色
        dataSet.setCircleColor(Color.BLACK);// 圆形折点的颜色
        dataSet.setHighLightColor(Color.GREEN); // 高亮的线的颜色
        dataSet.setHighlightEnabled(false);
        dataSet.setValueTextColor(Color.BLACK); //数值显示的颜色
        dataSet.setValueTextSize(11f);     //数值显示的大小
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawValues(true);
        LineDataSet dataSet1 = new LineDataSet(yValues2, lineName1);//用y轴的集合来设置参数
        dataSet1.setLineWidth(3.5f);
        dataSet1.setCircleSize(2f);
        dataSet1.setColor(Color.GREEN);
        dataSet1.setCircleColor(Color.BLACK);
        dataSet1.setHighLightColor(Color.GREEN);
        dataSet1.setHighlightEnabled(false);
        dataSet1.setValueTextColor(Color.BLACK);
        dataSet1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet1.setValueTextSize(12f);
        dataSet1.setDrawValues(true);
        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);
        lineData.addDataSet(dataSet1);
        return lineData;
    }
    /**
     * @Description:初始化图表的样式
     */
    public static void initDataStyle(LineChart lineChart, LineData lineData, Context context) {
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);
        lineChart.setDrawBorders(false); //在折线图上添加边框
        Description description=new Description();
        description.setText("");
        description.setTextAlign(Paint.Align.CENTER);
        description.setTextSize(10);
        description.setPosition(200, 150);
        lineChart.setDescription(description); //数据描述
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false); //表格颜色
        lineChart.setGridBackgroundColor(Color.GRAY & 0x70FFFFFF); //表格的颜色，设置一个透明度
        lineChart.setTouchEnabled(true); //可点击
        lineChart.setDragEnabled(true);  //可拖拽
        lineChart.setScaleEnabled(false);  //可缩放
        lineChart.setPinchZoom(false);
        lineChart.setDrawMarkers(false);
        lineChart.setBackgroundColor(Color.WHITE); //设置背景颜色

        lineChart.setData(lineData);
        Legend mLegend = lineChart.getLegend(); //设置标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.SQUARE); //样式
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.GRAY); //颜色
        lineChart.setVisibleXRange(1, 12);   //x轴可显示的坐标范围
        XAxis xAxis = lineChart.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
         xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);    //字体的颜色
        xAxis.setTextSize(12f); //字体大小
        xAxis.setGridColor(Color.GRAY);//网格线颜色
        xAxis.setDrawGridLines(false); //不显示网格线
        xAxis.setLabelCount(12);
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(12);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter()
        { @Override
            public String getFormattedValue(float value) {
                String lab;
                int index=(int)value;
                lab=Month.get(index);
                return lab;
            }
        });


        YAxis axisLeft = lineChart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = lineChart.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.GRAY); //字体颜色
        axisLeft.setTextSize(12f); //字体大小
//        axisLeft.setAxisMaxValue((float) max); //最大值
        axisLeft.setAxisMinimum(0f);
        axisLeft.setLabelCount(5);
//        axisLeft.setLabelCount(5, true); //显示格数
        axisLeft.setGridColor(Color.GRAY); //网格线颜色
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        //设置动画效果
        lineChart.animateY(1000, Easing.Linear);
        lineChart.animateX(1000, Easing.Linear);
        lineChart.invalidate();
        //lineChart.animateX(2500);  //立即执行动画
    }

    /**
     * @param name
     * @Description:设置折线的名称
     */
    public static void setLineName(String name) {
        lineName = name;
    }

    /**
     * @param name
     * @Description:设置另一条折线的名称
     */
    public static void setLineName1(String name) {
        lineName1 = name;
    }
    public static void setCount(int num){
        count=num;
    }

}