package in.programmeraki.glaty;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.programmeraki.glaty.roomdb.FeedData;

public class HistoryGraph extends AppCompatActivity  implements OnChartValueSelectedListener {

    static TextView from_date_tv, to_date_tv;

    static String iffromdateselect = "0";

    TextView nodata;

    private ArrayList<FeedData> feedDataArrayList = new ArrayList<>();
    private ArrayList<FeedData> feedWithinRange = new ArrayList<>();
    Button btnsubmit;

    String fromdate, todate;
    private BarChart mChart;
    float barWidth = 0.3f;

    ArrayList xVals = new ArrayList();
    ArrayList yVals1 = new ArrayList();
    ArrayList yVals2 = new ArrayList();
    ArrayList yVals3 = new ArrayList();
    ArrayList yVals4 = new ArrayList();
    ArrayList yVals5 = new ArrayList();
    ArrayList yVals6 = new ArrayList();

    BarData data;
    BarDataSet set1, set2, set3, set4, set5, set6;

    int dice1_total = 0;
    int dice2_total = 0;
    int dice3_total = 0;
    int dice4_total = 0;
    int dice5_total = 0;
    int dice6_total = 0;

    static long i_timeInMillis, f_timeInMillis, fdi_timeInMillis, tdf_timeInMillis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);

        mChart = findViewById(R.id.barchart);
        btnsubmit = findViewById(R.id.graph_btnsubmit);
        nodata = findViewById(R.id.no_data_tv);

        from_date_tv = (TextView) findViewById(R.id.from_date_tv);
        to_date_tv = (TextView) findViewById(R.id.to_date_tv);


        from_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iffromdateselect = "0";
                showTruitonDatePickerDialog(v);
            }
        });

        to_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iffromdateselect = "1";
                showTruitonDatePickerDialog(v);
            }
        });

        findViewById(R.id.back_iv).setOnClickListener(view -> {
            finish();
        });

        setUpChart();

        fetchAllFeedFromDB();
        mChart.setVisibility(View.GONE);
        nodata.setVisibility(View.VISIBLE);

//        View.OnClickListener rgClickListener = view -> {
//            switch (view.getId()){
//                case R.id.both_rg:
//                    toggleChartVisibility(pulseAndTempBoth);
//                    break;
//                case R.id.pulse_rg:
//                    toggleChartVisibility(pulseOnly);
//                    break;
//                case R.id.temp_rg:
//                    toggleChartVisibility(tempOnly);
//                    break;
//            }
//        };
//
//        findViewById(R.id.both_rg).setOnClickListener(rgClickListener);
//        findViewById(R.id.pulse_rg).setOnClickListener(rgClickListener);
//        findViewById(R.id.temp_rg).setOnClickListener(rgClickListener);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromdate = from_date_tv.getText().toString();
                todate = to_date_tv.getText().toString();
                showdata();
            }
        });
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String eVal = new SimpleDateFormat("HH:mm:ss").format(new Date((long) e.getX()));
        Toast.makeText(this, "time: " + eVal + ", value: " +e.getY(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            i_timeInMillis = c.getTimeInMillis();

            c.add(Calendar.HOUR, 24);
            f_timeInMillis = c.getTimeInMillis();

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

            return  dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (iffromdateselect.equals("0")) {
                from_date_tv.setText(day + "-" + (month + 1) + "-" + year);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String fromdate = from_date_tv.getText().toString();
                    Date mDate = sdf.parse(fromdate);
                    long timeInMilliseconds = mDate.getTime();

                    fdi_timeInMillis = timeInMilliseconds;


                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                fdi_timeInMillis = i_timeInMillis;
            } else {
                to_date_tv.setText(day + "-" + (month + 1) + "-" + year);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String todate = to_date_tv.getText().toString();
                    Date mDate = sdf.parse(todate);
                    long timeInMilliseconds = mDate.getTime();

                    tdf_timeInMillis = timeInMilliseconds;


                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }

//                tdf_timeInMillis = f_timeInMillis;
            }
        }


    public void showdata(){

        feedWithinRange = new ArrayList<>();
        for (FeedData feedData: feedDataArrayList) {
            if (feedData.getDate_time_in_millis() >= fdi_timeInMillis && feedData.getDate_time_in_millis() < tdf_timeInMillis) {
//            if (feedData.getDate_time_in_millis() < tdf_timeInMillis) {
                feedWithinRange.add(feedData);
            }
        }

        int feedcount = feedWithinRange.size();
        if(feedcount>0)
        {
            for (int i = 0; i < feedWithinRange.size(); i++) {
                mChart.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
                FeedData feedData = feedWithinRange.get(i);
                int diceval = Integer.parseInt(feedData.getPulse());
                appendBarChartData(diceval);
            }
        }
        else
        {
            mChart.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    void fetchAllFeedFromDB(){
        new FeedDataFetchAsyncTask(this).execute();
    }

    private class FeedDataFetchAsyncTask extends AsyncTask<Void, Void, Integer> {

        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private int value;
        SimpleDateFormat formatter1;
        private String TAG = "mCustomAsync";

        FeedDataFetchAsyncTask(Activity activity) {
            this.weakActivity = new WeakReference<>(activity);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            feedDataArrayList = (ArrayList<FeedData>) Common.instance.db.feedDataDao().getAllFeedData();
            return Common.instance.db.feedDataDao().getAllFeedData().size();
        }

        @Override
        protected void onPostExecute(Integer feedCount) {
            Log.d(TAG, "onPostExecute: feedsFetchedCount:" + feedCount);
//            updateUI();
        }
    }


        /*
         * Chart Methods
         */
        private void setUpChart(){

            mChart.setDescription(null);
            mChart.setPinchZoom(false);
            mChart.setScaleEnabled(false);
            mChart.setDrawBarShadow(false);
            mChart.setDrawGridBackground(false);


            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setYOffset(20f);
            l.setXOffset(0f);
            l.setYEntrySpace(0f);
            l.setTextSize(8f);

            //X-axis
            XAxis xAxis = mChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMaximum(7);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
            mChart.getAxisRight().setEnabled(false);
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setValueFormatter(new LargeValueFormatter());
            leftAxis.setDrawGridLines(true);
            leftAxis.setSpaceTop(35f);
            leftAxis.setAxisMinimum(0f);
//        appendBarChartData();
        }

        //    private void appendBarChartData(){
        private void appendBarChartData(int diceno){
//
            if(diceno == 1) {
                dice1_total = dice1_total + 1;
            } else if (diceno == 2) {
                dice2_total = dice2_total + 1;
            } else if (diceno == 3) {
                dice3_total = dice3_total + 1;
            } else if (diceno == 4) {
                dice4_total = dice4_total + 1;
            } else if (diceno == 5) {
                dice5_total = dice5_total + 1;
            } else {
                dice6_total = dice6_total + 1;
            }

            yVals1.add(new BarEntry(1, (float) dice1_total));
            yVals2.add(new BarEntry(2, (float) dice2_total));
            yVals3.add(new BarEntry(3, (float) dice3_total));
            yVals4.add(new BarEntry(4, (float) dice4_total));
            yVals5.add(new BarEntry(5, (float) dice5_total));
            yVals6.add(new BarEntry(6, (float) dice6_total));

//        BarDataSet set1, set2, set3, set4, set5, set6;

            set1 = new BarDataSet(yVals1, "1");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                set1.setColor(getColor(R.color.colorGlatyOrange));
            }

            set2 = new BarDataSet(yVals2, "2");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                set2.setColor(getColor(R.color.colorGlatyYellow));
            }

            set3 = new BarDataSet(yVals3, "3");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                set3.setColor(getColor(R.color.colorGlatyRed));
            }

            set4 = new BarDataSet(yVals4, "4");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                set4.setColor(getColor(R.color.colorGlatyPurple));
            }

            set5 = new BarDataSet(yVals5, "5");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                set5.setColor(getColor(R.color.colorGlatyGreen));
            }

            set6 = new BarDataSet(yVals6, "6");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                set6.setColor(getColor(R.color.colorGlatyBlue));
            }


            data = new BarData(set1, set2, set3, set4, set5, set6);
            data.setValueFormatter(new LargeValueFormatter());
            mChart.setData(data);
            mChart.getBarData().setBarWidth(barWidth);
            mChart.getXAxis().setAxisMinimum(0);
        }


}