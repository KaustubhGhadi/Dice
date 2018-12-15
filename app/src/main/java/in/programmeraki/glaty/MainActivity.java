package in.programmeraki.glaty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import in.programmeraki.glaty.fragment.HomeFragment;
import in.programmeraki.glaty.fragment.NotificationFragment;
import in.programmeraki.glaty.nrfkit.profile.BleProfileActivity;
import in.programmeraki.glaty.roomdb.AppDatabase;
import in.programmeraki.glaty.roomdb.FeedData;
import in.programmeraki.glaty.roomdb.NotificationData;
import in.programmeraki.glaty.utils.Constant;
import in.programmeraki.glaty.utils.HRSManager;
import in.programmeraki.glaty.utils.HRSManagerCallbacks;
import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.BleManagerCallbacks;

/**
 * Created by aishwarydhare on 01/02/18.
 */

public class MainActivity extends BleProfileActivity implements HRSManagerCallbacks {

    private final static String HR_VALUE = "hr_value";
    private final static String HR_POS = "hr_pos";
    private final static int MAX_HR_VALUE = 7;
    private final static int MIN_POSITIVE_VALUE = 1;
    private final static int REFRESH_INTERVAL = 1000; // 1 second interval
    public static boolean isAlive = false;
    private final String TAG = "mCustom";
    public View.OnClickListener nav_drawer_items_listener;
    FrameLayout fragment_fl;
    RelativeLayout mainView;
    ImageView drawer_icon;
    Activity activity;
    int activeColor = Color.parseColor("#d4b252");
    int inactiveColor = -1;
    boolean isFeedProcessing = false;
    private DrawerLayout mDrawerLayout;
    private boolean isDrawerOpen;
    private boolean wantToExit = false;
    private View bell_icon;
    private View action_connect;
    private ProgressBar progressBar;
    private boolean isNotificationProcessing = false;
    private int mHrmValue = 0;

    private BarChart Mchart;
    float barWidth = 0.3f;

    ImageView diceimg;

    ArrayList xVals = new ArrayList();
    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
    ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
    ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
    ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
    ArrayList<BarEntry> yVals5 = new ArrayList<BarEntry>();
    ArrayList<BarEntry> yVals6 = new ArrayList<BarEntry>();

    BarData data;
    BarDataSet set1, set2, set3, set4, set5, set6;

    int dice1_total = 0;
    int dice2_total = 0;
    int dice3_total = 0;
    int dice4_total = 0;
    int dice5_total = 0;
    int dice6_total = 0;

//    LinearLayout chartll;

    @Override
    protected void onPause() {
        super.onPause();
        isAlive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAlive = true;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        activity = this;

        setUpAppDatabase();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString(Constant.p_min, "").isEmpty()){
            editor.putString(Constant.p_min, Constant.p_min_default);
        }
        if(sharedPreferences.getString(Constant.p_max, "").isEmpty()){
            editor.putString(Constant.p_max, Constant.p_max_default);
        }
        if(sharedPreferences.getString(Constant.t_min, "").isEmpty()){
            editor.putString(Constant.t_min, Constant.t_min_default);
        }
        if(sharedPreferences.getString(Constant.t_max, "").isEmpty()){
            editor.putString(Constant.t_max, Constant.t_max_default);
        }
        if(sharedPreferences.getString(Constant.fname, "").isEmpty()){
            editor.putString(Constant.fname, "Mary");
        }
        if(sharedPreferences.getString(Constant.lname, "").isEmpty()){
            editor.putString(Constant.lname, "Jane");
        }
        if(sharedPreferences.getString(Constant.email, "").isEmpty()){
            editor.putString(Constant.email, "mary.jane@yourmail.co");
        }
        if(sharedPreferences.getString(Constant.dob, "").isEmpty()){
            editor.putString(Constant.dob, "09/07/1992");
        }
        editor.apply();

        mainView = findViewById(R.id.main_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
//        fragment_fl = findViewById(R.id.fragment_fl);

//        chartll = (LinearLayout) findViewById(R.id.chart_ll);
        Mchart = (BarChart)findViewById(R.id.barchart);
        diceimg = (ImageView) findViewById(R.id.dice_mainIV);
        setUpChart();
//        appendBarChartData(1);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                isDrawerOpen = false;
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                isDrawerOpen = true;
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                mainView.setTranslationX(slideOffset * drawerView.getWidth());
//                mDrawerLayout.bringChildToFront(drawerView);
//                mDrawerLayout.requestLayout();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        drawer_icon = findViewById(R.id.drawer_icon);
        View.OnClickListener drawer_listener = view -> {
            if(!isDrawerOpen){
                mDrawerLayout.openDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.closeDrawers();
            }
        };
        drawer_icon.setOnClickListener(drawer_listener);

        nav_drawer_items_listener = this::on_nav_drawer_item_selected;

        action_connect = findViewById(R.id.action_connect);
        bell_icon = findViewById(R.id.bell_icon);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.profile_v).setOnClickListener(nav_drawer_items_listener);
        findViewById(R.id.overview_v).setOnClickListener(nav_drawer_items_listener);
        findViewById(R.id.statistics_v).setOnClickListener(nav_drawer_items_listener);
        findViewById(R.id.calendar_v).setOnClickListener(nav_drawer_items_listener);
//        findViewById(R.id.schedule_v).setOnClickListener(nav_drawer_items_listener);
        findViewById(R.id.patient_hist_v).setOnClickListener(nav_drawer_items_listener);
        findViewById(R.id.newsfeed_v).setOnClickListener(nav_drawer_items_listener);
        findViewById(R.id.debug_v).setOnClickListener(nav_drawer_items_listener);

        progressBar.setVisibility(View.GONE);

        action_connect.setOnClickListener(view -> {
            Log.d(TAG, "onCreateView: clicked bluetooth button");
            super.onConnectClicked(view);
        });

        bell_icon.setOnClickListener(view -> {
            Log.d(TAG, "onCreateView: clicked bell icon");
            set_fragment(7);

        });

        final int[] i = {1};
        findViewById(R.id.simulate).setOnClickListener(view -> {
            if(i[0] > 7){
                i[0] = 1;
            }
            setHRSValueOnView(i[0]);
            i[0] += 1;
        });

        set_fragment(1);

        Bundle notification_bundle = getIntent().getExtras();
        if (notification_bundle != null && !notification_bundle.getString("notification", "").equalsIgnoreCase("")) {
            try {
                JSONObject jsonObject = new JSONObject(notification_bundle.getString("notification", ""));
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle(jsonObject.getString("title"))
                        .setMessage(jsonObject.getString("body"))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Oops, notify lapsed", Toast.LENGTH_SHORT).show();
            }
        }

//        yVals1.add(new BarEntry(1, (float) 1));
//        yVals2.add(new BarEntry(2, (float) 3));
//        yVals3.add(new BarEntry(3, (float) 5));
//        yVals4.add(new BarEntry(4, (float) 7));
//        yVals5.add(new BarEntry(5, (float) 9));
//        yVals6.add(new BarEntry(6, (float) 5));
//
////        BarDataSet set1, set2, set3, set4, set5, set6;
//
//
//        data = new BarData(set1, set2, set3, set4, set5, set6);
//        data.setValueFormatter(new LargeValueFormatter());
//        chart.setData(data);
//        chart.getBarData().setBarWidth(barWidth);
//        chart.getXAxis().setAxisMinimum(0);
//
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

    @SuppressLint("ResourceAsColor")
    void set_fragment(int id){
        Log.d(TAG, "set_fragment: constant.selected_frag: " + Constant.selected_frag_id);

        if(inactiveColor == -1){
            inactiveColor = ((TextView) findViewById(R.id.profile_v)).getCurrentTextColor();
        }

        ((TextView) findViewById(R.id.profile_v)).setTextColor(inactiveColor);
        ((TextView) findViewById(R.id.overview_v)).setTextColor(inactiveColor);
        ((TextView) findViewById(R.id.statistics_v)).setTextColor(inactiveColor);
        ((TextView) findViewById(R.id.calendar_v)).setTextColor(inactiveColor);
//        ((TextView) findViewById(R.id.schedule_v)).setTextColor(inactiveColor);
        ((TextView) findViewById(R.id.patient_hist_v)).setTextColor(inactiveColor);
        ((TextView) findViewById(R.id.newsfeed_v)).setTextColor(inactiveColor);
        ((TextView) findViewById(R.id.debug_v)).setTextColor(inactiveColor);

        if(id == Constant.selected_frag_id){
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            Log.d(TAG, "set_fragment: already active");
            return;
        }

        Fragment fragment = null;
        String frag_str = "";

        switch (id){
            case 1:
                fragment = new HomeFragment();
                frag_str = "HomeFragment";
                ((TextView) findViewById(R.id.profile_v)).setTextColor(activeColor);
                break;

            case 2:
                startActivity(new Intent(activity, CalendarActivity.class));
                break;

            case 3:
                startActivity(new Intent(activity, HistoryGraph.class));
                break;

            case 4:
                fragment = new NotificationFragment();
                frag_str = "NotificationFragment";
                break;

            case 5:
//                startActivity(new Intent(activity, ScheduleActivity.class));
                startActivity(new Intent(activity, AlarmActivity.class));
                break;

            case 6:
                startActivity(new Intent(activity, DebugActivity.class));
                break;

            case 7:
                startActivity(new Intent(activity, AlertsActivity.class));
                break;

            case 8:
                startActivity(new Intent(activity, Newsfeed.class));
                break;

            case 9:
                startActivity(new Intent(activity, Statistics.class));
                break;

//            case 6:
//                fragment = new ItemsListFragment();
//                fragment.setArguments(item_list_frag_bundle);
//                frag_str = "ItemsListFragment";
//                break;

            default:
//                fragment = new HomeFragment();
//                frag_str = "HomeFragment";
                break;
        }

        if(frag_str.equalsIgnoreCase("HomeFragment") || frag_str.isEmpty()){
            drawer_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer_blue));
        } else {
            drawer_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer));
        }

        if (fragment != null) {
            if(getSupportFragmentManager().getBackStackEntryCount() == 0 && !frag_str.equalsIgnoreCase("HomeFragment")){
//                getSupportFragmentManager().beginTransaction().addToBackStack(frag_str).replace(R.id.fragment_fl, fragment, frag_str).commit();
            } else {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_fl, fragment, frag_str).commit();
            }
        }

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    public void on_nav_drawer_item_selected(View view){

        switch (view.getId()){
            case R.id.profile_v:
                set_fragment(1);
                break;

            case R.id.overview_v:
                set_fragment(1);
                break;

            case R.id.calendar_v:
                set_fragment(2);
                break;

            case R.id.debug_v:
                set_fragment(6);
                break;

//            case R.id.schedule_v:
//                set_fragment(5);
//                break;

            case R.id.patient_hist_v:
//                set_fragment(4);
                set_fragment(3);
                break;

            case R.id.newsfeed_v:
                set_fragment(8);
                break;

            case R.id.statistics_v:
                set_fragment(9);
                break;

            default:
                // do nothing
                break;
        }
    }


    public void sign_out_user(){
        //do nothing
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }


        if(Constant.selected_frag_id == 1){
            if (wantToExit) {
                try {
                    super.onBackPressed();
                    Constant.selected_frag_id = 0;
                    Common.instance = null;
                    this.finishAffinity();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                wantToExit = true;
            }

            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> wantToExit = false, 2000);
        } else {
            super.onBackPressed();
        }

//        if(Constant.selected_frag_id == 1){
//            drawer_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer));
//        }
    }


    /*
     * BLE implemented methods
     */
    @Override
    protected BleManager<? extends BleManagerCallbacks> initializeManager() {
        final HRSManager manager = HRSManager.getInstance(getApplicationContext());
        manager.setGattCallbacks(this);
        return manager;
    }

    @Override
    protected void setDefaultUI() {
        Log.d(TAG, "setDefaultUI: ");
    }

    @Override
    protected int getDefaultDeviceName() {
        Log.d(TAG, "getDefaultDeviceName: ");
        return R.string.hrs_default_name;
    }

    @Override
    protected int getAboutTextId() {
        Log.d(TAG, "getAboutTextId: ");
        return R.string.hrs_about_text;
    }

    @Override
    protected UUID getFilterUUID() {
        Log.d(TAG, "getFilterUUID: ");
        return HRSManager.HR_SERVICE_UUID;
    }

    /*
        this.timestamp = timestamp;
        this.feed_val = feed_val;
        this.feed_type = feed_type;
        this.pulse = pulse;
        this.temp = temp;
        this.raw_data = raw_data;
        this.date_time_in_millis = date_time_in_millis;
     */

    /*
     * Custom Methods
     * */

    private void setHRSValueOnView(final int value) {
        Log.d(TAG, "setHRSValueOnView: ");
        if (value < MIN_POSITIVE_VALUE || value > MAX_HR_VALUE) {
            return;
        }
//        runOnUiThread(() -> {
//            Log.d(TAG, "setHRSValueOnView: " + value);
//            insertFeedDataInDb(value);
//        });

//        Mchart.setVisibility(View.VISIBLE);
//        appendBarChartData(value);
        storeInDB(value);
//        reload();

    }

    private void setUpAppDatabase() {
        Common.instance.db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "glaty_db").build();
    }

    void insertFeedDataInDb(int value){
        if(isFeedProcessing){
            Log.d(TAG, "insertFeedDataInDb: previous feed is still under processing");
            return;
        }
        isFeedProcessing = true;
//        new FeedDataInsertAsyncTask(activity, value).execute();
    }

    void insertNotificationsInDb(String title, String message){
        if(isNotificationProcessing){
            Log.d(TAG, "insertNotificationsInDb: previous notification is still under processing");
            return;
        }
        isNotificationProcessing = true;
        new NotificationInsertAsyncTask(activity, title, message).execute();
    }

    void showDialog(String msg){
        runOnUiThread(() -> {
            final Dialog dialog = new Dialog(activity);
            if (dialog.getActionBar() != null) {
                dialog.getActionBar().hide();
            }
            if (dialog.getWindow() != null) {
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            dialog.setContentView(R.layout.dialog_box_simple_order_confirmed);
            TextView txtNext = (TextView) dialog.findViewById(R.id.txt_next);
            TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_title);
            TextView message = (TextView) dialog.findViewById(R.id.message);
            LinearLayout root_ll = dialog.findViewById(R.id.root_ll);
            ImageView iv = dialog.findViewById(R.id.iv);

            txtTitle.setText("Alert!");
            txtNext.setText("Ok");
            message.setText(msg);

            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            ViewGroup.LayoutParams layoutParams = root_ll.getLayoutParams();
            layoutParams.width = (int) (width*0.80);
            dialog.show();

            insertNotificationsInDb(txtTitle.getText().toString(), message.getText().toString());

            txtNext.setOnClickListener(v -> dialog.dismiss());

            try {
                if(Constant.selected_frag_id == 1){
                    if(getSupportFragmentManager().findFragmentByTag("HomeFragment") != null){
                        HomeFragment frag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
                        frag.fetchAllFeedFromDB();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /*
     * HRS Manager Callbacks
     * */
    @Override
    public void onHRSensorPositionFound(BluetoothDevice device, String position) {
        Log.d(TAG, "onHRSensorPositionFound: " + device.getAddress() + ", pos: " + position);
    }

    @Override
    public void onHRValueReceived(BluetoothDevice device, int value) {
        Log.d(TAG, "onHRValueReceived: " + device.getAddress() + ", val: " + value);
        mHrmValue = value;
        setHRSValueOnView(mHrmValue);
    }


    /*
     * BleActivity Override Methods
     * */
    @Override
    public void onServicesDiscovered(BluetoothDevice device, boolean optionalServicesFound) {
        super.onServicesDiscovered(device, optionalServicesFound);
        Log.d(TAG, "onServicesDiscovered: ");
    }

    @Override
    public void onDeviceConnected(BluetoothDevice device) {
        super.onDeviceConnected(device);
        runOnUiThread(() ->{
            progressBar.setVisibility(View.GONE);
//            action_connect.setVisibility(View.GONE);
            action_connect.setBackgroundResource(R.drawable.bluetooth_off);
            Toast.makeText(this, "Device Connected", Toast.LENGTH_SHORT).show();
            set_fragment(4);
        });
    }

    @Override
    public void onDeviceReady(BluetoothDevice device) {
        super.onDeviceReady(device);
        Log.d(TAG, "onDeviceReady: ");
    }

    @Override
    public void onDeviceDisconnected(final BluetoothDevice device) {
        super.onDeviceDisconnected(device);
        Log.d(TAG, "onDeviceDisconnected: ");
        runOnUiThread(() -> {
            Toast.makeText(this, "Device Disconnected", Toast.LENGTH_SHORT).show();
//            action_connect.setVisibility(View.VISIBLE);
            action_connect.setBackgroundResource(R.drawable.ble_img);
        });
    }

    @Override
    public void onDeviceConnecting(BluetoothDevice device) {
        super.onDeviceConnecting(device);
        runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(HR_VALUE, mHrmValue);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHrmValue = savedInstanceState.getInt(HR_VALUE);
    }

    private void storeInDB(int pulseVal){

        Date dtNow = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate= formatter.format(dtNow);

        new FeedDataInsertAsyncTask(activity, pulseVal).execute();
    }

    private class FeedDataInsertAsyncTask extends AsyncTask<Void, Void, Integer> {

        SimpleDateFormat formatter1;
        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private int value;
        private String TAG = "mCustomAsync";

        FeedDataInsertAsyncTask(Activity activity, int value) {
            this.weakActivity = new WeakReference<>(activity);
            this.value = value;
            formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        }

        @Override
        protected Integer doInBackground(Void... params) {

            /* code save data for old date  */
//            String datetTimeStr = "17/10/2018 16:08:28";
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
//            try {
//                Date mDate = sdf.parse(datetTimeStr);
//                long timeInMilliseconds = mDate.getTime();
////                System.out.println("Date in milli :: " + timeInMilliseconds);
//
//                Common.instance.db.feedDataDao().insertAll(new FeedData(
//                        datetTimeStr,
//                        String.valueOf(value),
//                        String.valueOf(Common.instance.getFeedAreaType(value)),
//                        String.valueOf(value),
//                        String.valueOf(0),
//                        String.valueOf(value),
//                        timeInMilliseconds
//                ));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


            /* code save data for Current date */
            String datetTimeStr = formatter1.format(Calendar.getInstance().getTime());
            Common.instance.db.feedDataDao().insertAll(new FeedData(
                    datetTimeStr,
                    String.valueOf(value),
                    String.valueOf(Common.instance.getFeedAreaType(value)),
                    String.valueOf(value),
                    String.valueOf(0),
                    String.valueOf(value),
                    Calendar.getInstance().getTimeInMillis()
            ));
            return Common.instance.db.feedDataDao().getAllFeedData().size();
        }

        @Override
        protected void onPostExecute(Integer feedCount) {
            isFeedProcessing = false;
//            Log.d(TAG, "onPostExecute: total entries in db now: " + feedCount);
//            showDialog(
//                    "It's a " + Common.instance.getAreaName(value) + "."
//            );

            appendBarChartData(value);
        }

    }

    private class NotificationInsertAsyncTask extends AsyncTask<Void, Void, Integer> {

        SimpleDateFormat formatter1;
        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private String message;
        private String title;
        private String TAG = "mCustomAsync";

        NotificationInsertAsyncTask(Activity activity, String title, String message) {
            this.weakActivity = new WeakReference<>(activity);
            this.title = title;
            this.message = message;
            formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        }

        @Override
        protected Integer doInBackground(Void... params) {

            String datetTimeStr = formatter1.format(Calendar.getInstance().getTime());
            Common.instance.db.notificationDataDao().insertAll(new NotificationData(
                    datetTimeStr,
                    title,
                    message,
                    Calendar.getInstance().getTimeInMillis()
            ));
            return Common.instance.db.notificationDataDao().getAllNotifications().size();
        }

        @Override
        protected void onPostExecute(Integer notiffCount) {
            isNotificationProcessing = false;
            Log.d(TAG, "onPostExecute: total notifications in db now: " + notiffCount);
        }

    }


    /*
     * Chart Methods
     */
    private void setUpChart(){

        Mchart.setDescription(null);
        Mchart.setPinchZoom(false);
        Mchart.setScaleEnabled(false);
        Mchart.setDrawBarShadow(false);
        Mchart.setDrawGridBackground(false);



        Legend l = Mchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = Mchart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(7);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
        Mchart.getAxisRight().setEnabled(false);
        YAxis leftAxis = Mchart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        Mchart.animateY(2000);

//        appendBarChartData();
    }

//    private void appendBarChartData(){
    private void appendBarChartData(int diceno){

        if(diceno == 1) {
            diceimg.setImageResource(R.drawable.dice1);
        } else if (diceno == 2) {
            diceimg.setImageResource(R.drawable.dice2);
        } else if (diceno == 3) {
            diceimg.setImageResource(R.drawable.dice3);
        } else if (diceno == 4) {
            diceimg.setImageResource(R.drawable.dice4);
        } else if (diceno == 5) {
            diceimg.setImageResource(R.drawable.dice5);
        } else {
            diceimg.setImageResource(R.drawable.dice6);
        }

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

    /* Show data*/
        BarEntry BarEntry1 = new BarEntry(1, (float) dice1_total);
        BarEntry BarEntry2 = new BarEntry(2, (float) dice2_total);
        BarEntry BarEntry3 = new BarEntry(3, (float) dice3_total);
        BarEntry BarEntry4 = new BarEntry(4, (float) dice4_total);
        BarEntry BarEntry5 = new BarEntry(5, (float) dice5_total);
        BarEntry BarEntry6 = new BarEntry(6, (float) dice6_total);

        yVals1.add(BarEntry1);
        yVals2.add(BarEntry2);
        yVals3.add(BarEntry3);
        yVals4.add(BarEntry4);
        yVals5.add(BarEntry5);
        yVals6.add(BarEntry6);

//        if( Mchart.getData() != null && Mchart.getData().getDataSetCount() > 0){
//
//            Mchart.getData().getDataSets().get(0).addEntry(BarEntry1);
//            Mchart.getData().getDataSets().get(0).addEntry(BarEntry2);
//            Mchart.getData().getDataSets().get(0).addEntry(BarEntry3);
//            Mchart.getData().getDataSets().get(0).addEntry(BarEntry4);
//            Mchart.getData().getDataSets().get(0).addEntry(BarEntry5);
//            Mchart.getData().getDataSets().get(0).addEntry(BarEntry6);
//
//            Mchart.getData().notifyDataChanged();
//            Mchart.notifyDataSetChanged();
//            Mchart.invalidate();
//            Mchart.refreshDrawableState();
//            return;
//        }

        /*data*/
//        yVals1.add(new BarEntry(1, (float) dice1_total));
//        yVals2.add(new BarEntry(2, (float) dice2_total));
//        yVals3.add(new BarEntry(3, (float) dice3_total));
//        yVals4.add(new BarEntry(4, (float) dice4_total));
//        yVals5.add(new BarEntry(5, (float) dice5_total));
//        yVals6.add(new BarEntry(6, (float) dice6_total));


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
        data.notifyDataChanged();
        Mchart.setData(data);
        Mchart.getBarData().setBarWidth(barWidth);
        Mchart.getXAxis().setAxisMinimum(0);
        Mchart.getData().notifyDataChanged();
        Mchart.notifyDataSetChanged();
        Mchart.invalidate();
        return;
    }

}
