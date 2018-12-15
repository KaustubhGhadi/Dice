package in.programmeraki.glaty;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Newsfeed extends AppCompatActivity {

    private BarChart chart;
    float barWidth = 0.3f;
    float barSpace = 0f;
    float groupSpace = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        findViewById(R.id.back_iv).setOnClickListener(view -> {
            finish();
        });


        findViewById(R.id.newsfeed_ll1).setOnClickListener(view -> {
            String url = getResources().getString( R.string.url1 );
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        findViewById(R.id.newsfeed_ll2).setOnClickListener(view -> {
            String url = getResources().getString( R.string.url2 );
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        findViewById(R.id.newsfeed_ll3).setOnClickListener(view -> {
            String url = getResources().getString( R.string.url3 );
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        /* Bar Chat code */
//        chart = (BarChart)findViewById(R.id.barchart);
//        chart.setDescription(null);
//        chart.setPinchZoom(false);
//        chart.setScaleEnabled(false);
//        chart.setDrawBarShadow(false);
//        chart.setDrawGridBackground(false);
//
//
//        ArrayList xVals = new ArrayList();
//
////        xVals.add("1");
////        xVals.add("2");
////        xVals.add("3");
////        xVals.add("4");
////        xVals.add("5");
////        xVals.add("6");
//
//        ArrayList yVals1 = new ArrayList();
//        ArrayList yVals2 = new ArrayList();
//        ArrayList yVals3 = new ArrayList();
//        ArrayList yVals4 = new ArrayList();
//        ArrayList yVals5 = new ArrayList();
//        ArrayList yVals6 = new ArrayList();
//
//        yVals1.add(new BarEntry(1, (float) 1));
//        yVals2.add(new BarEntry(2, (float) 3));
//        yVals3.add(new BarEntry(3, (float) 5));
//        yVals4.add(new BarEntry(4, (float) 7));
//        yVals5.add(new BarEntry(5, (float) 9));
//        yVals6.add(new BarEntry(6, (float) 5));
//
////        yVals2.add(new BarEntry(1, (float) 2));
////        yVals2.add(new BarEntry(2, (float) 4));
////        yVals2.add(new BarEntry(3, (float) 6));
////        yVals2.add(new BarEntry(4, (float) 8));
////        yVals2.add(new BarEntry(5, (float) 10));
////        yVals2.add(new BarEntry(6, (float) 12));
//
//        BarDataSet set1, set2, set3, set4, set5, set6;
//        set1 = new BarDataSet(yVals1, "1");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            set1.setColor(getColor(R.color.colorGlatyOrange));
//        }
//
//        set2 = new BarDataSet(yVals2, "2");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            set2.setColor(getColor(R.color.colorGlatyYellow));
//        }
//
//        set3 = new BarDataSet(yVals3, "3");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            set3.setColor(getColor(R.color.colorGlatyRed));
//        }
//
//        set4 = new BarDataSet(yVals4, "4");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            set4.setColor(getColor(R.color.colorGlatyPurple));
//        }
//
//        set5 = new BarDataSet(yVals5, "5");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            set5.setColor(getColor(R.color.colorGlatyGreen));
//        }
//
//        set6 = new BarDataSet(yVals6, "6");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            set6.setColor(getColor(R.color.colorGlatyBlue));
//        }
//
//        BarData data = new BarData(set1, set2, set3, set4, set5, set6);
//        data.setValueFormatter(new LargeValueFormatter());
//        chart.setData(data);
//        chart.getBarData().setBarWidth(barWidth);
//        chart.getXAxis().setAxisMinimum(0);
////        chart.getXAxis().setAxisMaximum(10);
////        chart.groupBars(0, groupSpace, barSpace);
//
//        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setYOffset(20f);
//        l.setXOffset(0f);
//        l.setYEntrySpace(0f);
//        l.setTextSize(8f);
//
//        //X-axis
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMaximum(7);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
////Y-axis
//        chart.getAxisRight().setEnabled(false);
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setValueFormatter(new LargeValueFormatter());
//        leftAxis.setDrawGridLines(true);
//        leftAxis.setSpaceTop(35f);
//        leftAxis.setAxisMinimum(0f);
    }
}
