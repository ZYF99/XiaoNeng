package com.zhangyf.xiaoneng;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.orhanobut.hawk.Hawk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.zhangyf.xiaoneng.MainActivity.STUDENT_LIST_KEY;

public class DetailActivity extends AppCompatActivity {

    public static final String STUDENT_ID_KEY = "studentId";
    private Student globalStudent;
    private BarChart barChart;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        barChart = findViewById(R.id.bar_chart);
        final String studentId = getIntent().getStringExtra(STUDENT_ID_KEY);
        if (studentId!=null){
            //通过id找学生
            ((List<Student>)Hawk.get(STUDENT_LIST_KEY)).forEach(new Consumer<Student>() {
                @Override
                public void accept(Student student) {
                    if(student.getId() .equals(studentId) ){
                        globalStudent = student;
                    }
                }
            });
            //画图表
            setHBarChartData(globalStudent.getIssueList());
        }

    }

    private void setUpLineChart(){
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);

        //显示边框
        barChart.setDrawBorders(true);
        //设置动画效果
//        lineChart.animateY(1000,);
//        lineChart.animateX(1000,);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMinimum(0f);
//        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setAxisMinimum(0f);
//        rightAxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        //不显示表边框
        barChart.setDrawBorders(false);
//        不显示右下角描述内容
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
//        不显示X轴 Y轴线条
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        leftAxis.setEnabled(false);

    }

    /**
     * 设置水平柱形图数据的方法
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setHBarChartData(List<Issue> issueList) {
        List<Student> students = Hawk.get(STUDENT_LIST_KEY);
        final int studentNum = students.size();

        //填充数据，在这里换成自己的数据源
        final ArrayList<BarEntry> personalYValues = new ArrayList<>();
        final ArrayList<BarEntry> averageYValues = new ArrayList<>();

        barChart.getAxisRight().setEnabled(false);
        barChart.setDragEnabled(false);
barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
barChart.setDescription(null);
        //平均数据
        final HashMap<Integer,Integer> issueStatusMap = new HashMap<>();
        students.forEach(new Consumer<Student>() {
            @Override
            public void accept(Student student) {
                student.getIssueList().forEach(new Consumer<Issue>() {
                    @Override
                    public void accept(Issue issue) {
                        int originValue;
                        try {
                            originValue= issueStatusMap.get(issue.getId());
                        }catch (Exception e){
                            originValue = 0;
                        }
                        issueStatusMap.put(issue.getId(),originValue+issue.getStatus());
                    }
                });
            }
        });
        //真实的平均数据
        final List<IntFloatPair>averageList =new ArrayList<>();
        issueStatusMap.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer integer, Integer integer2) {
                ;
                averageList.add(new IntFloatPair(integer,Float.parseFloat(txfloat(integer2,studentNum))));
            }
        });


        //个人题目数据
        issueList.forEach(new Consumer<Issue>() {
            @Override
            public void accept(Issue issue) {
                personalYValues.add(new BarEntry(issue.getId(), issue.getStatus()));
            }
        });

        //平均题目数据
        averageList.forEach(new Consumer<IntFloatPair>() {
            @Override
            public void accept(IntFloatPair intFloatPair) {
                averageYValues.add(new BarEntry(intFloatPair.getValue1(),intFloatPair.getValue2()));
            }
        });

        BarDataSet personalSet;
        BarDataSet averageSet;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            personalSet = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            averageSet = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            personalSet.setValues(personalYValues);
            averageSet.setValues(averageYValues);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            personalSet = new BarDataSet(personalYValues, "个人答题状况");
            averageSet = new BarDataSet(averageYValues, "平均答题状况");

            personalSet.setColor(Color.BLUE);
            averageSet.setColor(Color.YELLOW);
            personalSet.setDrawIcons(false);
            averageSet.setDrawIcons(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(personalSet);
            dataSets.add(averageSet);
            BarData data = new BarData(dataSets);
 /*           data.setValueTextSize(10f);
            data.setBarWidth(0.5f);*/
            int barAmount = 2; //需要显示柱状图的类别 数量
//设置组间距占比30% 每条柱状图宽度占比 70% /barAmount  柱状图间距占比 0%
            float groupSpace = 0.3f; //柱状图组之间的间距
            float barSpace = 0f;
            float barWidth = (1f - groupSpace) / barAmount;
            //设置柱状图宽度
            data.setBarWidth(barWidth);
            data.groupBars(0f, groupSpace, barSpace);
            barChart.setData(data);
        }
    }
    public static String txfloat(int a,int b) {
        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数

        return df.format((float)a/b);

    }

}

class IntFloatPair {
    private int value1;
    private float value2;

    public IntFloatPair(int value1, float value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }
}
