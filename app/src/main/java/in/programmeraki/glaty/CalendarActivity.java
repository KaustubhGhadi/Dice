package in.programmeraki.glaty;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.programmeraki.glaty.adapter.CalendarFeedAdapter;
import in.programmeraki.glaty.decorators.EventDecorator;
import in.programmeraki.glaty.model.CalenderFeed;
import in.programmeraki.glaty.roomdb.FeedData;

public class CalendarActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    String[] weekLabelArr = new String[]{
            "Su", "M", "T", "W", "Th", "F", "S",
    };

    String[] monthLabelArr = new String[]{
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
    };

    MaterialCalendarView calendarView;
    Spinner month_spinner;
//    RecyclerView rv;
    CalendarFeedAdapter adapter;
    boolean isFirstRun = true;
    private Activity activity;
    private TextView noData;
    private ViewGroup progressBar;
    private ArrayList<FeedData> feedDataArrayList = new ArrayList<>();
    private ArrayList<FeedData> feedWithinRange = new ArrayList<>();
    private String TAG = "mCustom";

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        activity = this;

        calendarView = findViewById(R.id.calendarView);
        month_spinner = findViewById(R.id.month_spinner);
//        rv = findViewById(R.id.rv);
        noData = findViewById(R.id.no_data_tv);
        progressBar = findViewById(R.id.progressBar);
        mChart = findViewById(R.id.chart);

        findViewById(R.id.back_iv).setOnClickListener(view -> {
            finish();
        });

        progressBar.setVisibility(View.VISIBLE);
        calendarView.setVisibility(View.GONE);
//        rv.setVisibility(View.GONE);

        setUpChart();

        setupSpinner();

        fetchAllFeedFromDB();

        isFirstRun = true;
    }

    private void setupRecyclerView() {
//        ArrayList<CalenderFeed> calenderFeedArrayList = new ArrayList<>();
//        int[] areasTemp = new int[]{
//                Constant.AREA_RIGHT_ARM,
//                Constant.AREA_LEFT_ARM,
//                Constant.AREA_RIGHT_THIGH,
//                Constant.AREA_LEFT_THIGH,
//                Constant.AREA_RIGHT_BUTT,
//                Constant.AREA_LEFT_BUTT,
//                Constant.AREA_STOMACH
//        };
//
//        Calendar c = Calendar.getInstance();
//        int area;
//        Random generator = new Random();
//        for (int i = 0; i < 12; i++) {
//            c.set(Calendar.HOUR, i*2);
//            area = areasTemp[generator.nextInt(areasTemp.length)];
//            calenderFeedArrayList.add(new CalenderFeed(c, area));
//        }
        adapter = new CalendarFeedAdapter(new ArrayList<>());

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rv.setLayoutManager(llm);
//        rv.setAdapter(adapter);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.item_spinner, monthLabelArr);
        month_spinner.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        month_spinner.setSelection(month);
        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calendar.set(Calendar.MONTH, i);
                calendarView.setCurrentDate(CalendarDay.from(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        for (int i = 0; i < 1; i++) {
            if(dayOfMonth-i != 0){
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth-i);
                calendarView.setSelectedDate(CalendarDay.from(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
            }
        }
    }

    private void setupCalendarView() {
        calendarView.setWeekDayLabels(weekLabelArr);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        calendarView.setTopbarVisible(false);
        calendarView.setAllowClickDaysOutsideCurrentMonth(false);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_NONE);

        calendarView.setOnMonthChangedListener((materialCalendarView, calendarDay) -> {
            month_spinner.setSelection(calendarDay.getMonth());
        });


//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.MONTH, 1);
//        c.set(Calendar.DAY_OF_MONTH, 1);
//        calendarView.state().edit().setMinimumDate(CalendarDay.from(c.get(Calendar.YEAR),
//                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));
//
//        c.set(Calendar.MONTH, 12);
//        c.set(Calendar.DAY_OF_MONTH, 31);
//        calendarView.state().edit().setMaximumDate(CalendarDay.from(c.get(Calendar.YEAR),
//                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));

        calendarView.setOnDateChangedListener((materialCalendarView, calendarDay, b) -> {
            Log.d(TAG, "setupCalendarView: date selected: " + calendarDay.getDate().toString());

            Calendar c;
            c = Calendar.getInstance();
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.YEAR, calendarDay.getYear());
            c.set(Calendar.MONTH, calendarDay.getMonth());
            c.set(Calendar.DAY_OF_MONTH, calendarDay.getDay());
            long i_timeInMillis = c.getTimeInMillis();

            c.add(Calendar.HOUR, 24);
            long f_timeInMillis = c.getTimeInMillis();

            feedWithinRange = new ArrayList<>();
            for (FeedData feedData: feedDataArrayList) {
                if (feedData.getDate_time_in_millis() >= i_timeInMillis && feedData.getDate_time_in_millis() < f_timeInMillis) {
                    feedWithinRange.add(feedData);
                }
            }

            int feedcount = feedWithinRange.size();
            if(feedcount>0)
            {
                yVals1.clear();
                yVals2.clear();
                yVals3.clear();
                yVals4.clear();
                yVals5.clear();
                yVals6.clear();

                dice1_total = 0;
                dice2_total = 0;
                dice3_total = 0;
                dice4_total = 0;
                dice5_total = 0;
                dice6_total = 0;

                mChart.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);


                for (int i = 0; i < feedWithinRange.size(); i++) {
                    mChart.setVisibility(View.VISIBLE);
                    FeedData feedData = feedWithinRange.get(i);
                    int diceval = Integer.parseInt(feedData.getPulse());
                    appendBarChartData(diceval);
                }
            }
            else
            {
                yVals1.clear();
                yVals2.clear();
                yVals3.clear();
                yVals4.clear();
                yVals5.clear();
                yVals6.clear();

                dice1_total = 0;
                dice2_total = 0;
                dice3_total = 0;
                dice4_total = 0;
                dice5_total = 0;
                dice6_total = 0;

                mChart.setVisibility(View.GONE);
            }
        });


        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        calendarView.setSelectedDate(CalendarDay.from(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        ));

        long i_timeInMillis = c.getTimeInMillis();

        c.add(Calendar.HOUR, 24);
        long f_timeInMillis = c.getTimeInMillis();

        feedWithinRange = new ArrayList<>();
        for (FeedData feedData : feedDataArrayList) {
            if (feedData.getDate_time_in_millis() >= i_timeInMillis && feedData.getDate_time_in_millis() < f_timeInMillis) {
                feedWithinRange.add(feedData);
            }
        }


        for (int i = 0; i < feedDataArrayList.size(); i++) {
            mChart.setVisibility(View.VISIBLE);
            FeedData nfeedData = feedDataArrayList.get(i);
            int diceval = Integer.parseInt(nfeedData.getPulse());
            appendBarChartData(diceval);
        }

        noData.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void updateUI(){
        runOnUiThread(() -> {
            if(isFirstRun) {
                calendarView.setVisibility(View.VISIBLE);
                setupRecyclerView();
                setupCalendarView();
                isFirstRun = false;
            }

            updateRv();
        });
    }

    public void updateRv(){
        progressBar.setVisibility(View.GONE);
        if(feedWithinRange.size() < 1){
            findViewById(R.id.no_data_tv).setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
            return;
        } else {
            findViewById(R.id.no_data_tv).setVisibility(View.GONE);
            mChart.setVisibility(View.VISIBLE);
        }

        ArrayList<CalenderFeed> arr = new ArrayList<>();
        for (FeedData feed : feedWithinRange) {
            arr.add(new CalenderFeed(feed.getDate_time_in_millis(), feed.getFeed_val()));
        }
        adapter.setFeedArr(arr);
        adapter.notifyDataSetChanged();
        mChart.setVisibility(View.VISIBLE);
    }

    public void fetchAllFeedFromDB(){
        new FeedDataFetchAsyncTask(this).execute();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String eVal = new SimpleDateFormat("HH:mm:ss").format(new Date((long) e.getX()));
        Toast.makeText(this, "time: " + eVal + ", value: " +e.getY(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    private class FeedDataFetchAsyncTask extends AsyncTask<Void, Void, List<CalendarDay>> {

        SimpleDateFormat formatter1;
        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private int value;
        private String TAG = "mCustomAsync";

        FeedDataFetchAsyncTask(Activity activity) {
            this.weakActivity = new WeakReference<>(activity);
        }

//        @Override
//        protected Integer doInBackground(Void... params) {
//            feedDataArrayList = (ArrayList<FeedData>) Common.instance.db.feedDataDao().getAllFeedData();
//            return Common.instance.db.feedDataDao().getAllFeedData().size();
//        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* Test code */
//            LocalDate temp = LocalDate.now().minusMonths(2);
//            final ArrayList<CalendarDay> dates = new ArrayList<>();
//            for (int i = 0; i < 30; i++) {
//                final CalendarDay day = CalendarDay.from(temp);
//                dates.add(day);
//                temp = temp.plusDays(5);
//            }

            /* old code */
//            for (int i = 0; i < 30; i++) {
//                final CalendarDay day = CalendarDay.from(temp);
//                dates.add(day);
//                temp = temp.plusDays(5);
//            }
//
//            @Override
//            protected void onPostExecute(Integer feedCount) {
//                Log.d(TAG, "onPostExecute: feedsFetchedCount:" + feedCount);
//
//                calendarView.addDecorator(new EventDecorator(Color.RED, feedCount));
//                updateUI();
//            }


            /* get today date code */
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            //convert String to LocalDate
//            Calendar c = Calendar.getInstance();
//                        // manipulate date
//            c.add(Calendar.YEAR, 0);
//            c.add(Calendar.MONTH, -1);
//            c.add(Calendar.DATE, 0); //same with c.add(Calendar.DAY_OF_MONTH, 1);
//              DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                Date date = c.getTime();
//                String strDate = dateFormat.format(date);
            /**/

            /* new code 8 */
            final ArrayList<CalendarDay> dates = new ArrayList<>();
            feedDataArrayList = (ArrayList<FeedData>) Common.instance.db.feedDataDao().getAllFeedData();
            for (FeedData feedData: feedDataArrayList) {
                String sdate =  feedData.getTimestamp();
                sdate = sdate.substring(0, sdate.length() - 9);
                String dd = sdate.substring( 0, sdate.indexOf("/"));

                String allmm = sdate.substring(sdate.indexOf("/")+1, sdate.length());
                String mm = allmm.substring( 0, allmm.indexOf("/"));

                String yy = allmm.substring(allmm.indexOf("/")+1, allmm.length());

                if(mm.equals("01"))
                {
                    mm = "12";
                }
                else
                {
                    int imm = Integer.parseInt(mm);
                    imm = imm - 1;
                    String nmm = String.valueOf(imm);
                    if(!nmm.equals("10") || !nmm.equals("11"))
                    {
                        mm = "0" + nmm;
                    }
                }

                sdate = dd + "/" + mm + "/" + yy;
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                LocalDate localDate = LocalDate.parse(sdate, formatter);

                /**/
                Calendar c = Calendar.getInstance();
                // manipulate date
                c.add(Calendar.YEAR, 0);
                c.add(Calendar.MONTH, -1);
                c.add(Calendar.DATE, 0); //same with c.add(Calendar.DAY_OF_MONTH, 1);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = c.getTime();
                String ndate = dateFormat.format(date);


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
//
//                String nsdate = "19/09/2018";
                String nsdate = sdate;

                //convert String to LocalDate
                LocalDate localDate = LocalDate.parse(nsdate, formatter);
                final CalendarDay day = CalendarDay.from(localDate);
                dates.add(day);
            }

//            feedDataArrayList = (ArrayList<FeedData>) Common.instance.db.feedDataDao().getAll();
//            return Common.instance.db.feedDataDao().getAll().size();

            return dates;


        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
            updateUI();
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

//        dice1_total = 2;
//        dice2_total = 5;
//        dice3_total = 2;
//        dice4_total = 5;
//        dice5_total = 2;
//        dice6_total = 5;


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
