package in.programmeraki.glaty;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import in.programmeraki.glaty.adapter.AlertsAdapter;
import in.programmeraki.glaty.roomdb.NotificationData;

public class AlertsActivity extends AppCompatActivity {

    TextView noData;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    AlertsAdapter alertsAdapter;
    private ArrayList<NotificationData> notificationDataArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        recyclerView = findViewById(R.id.rv);
        progressBar = findViewById(R.id.progressBar);
        noData = findViewById(R.id.no_data_tv);

        findViewById(R.id.back_iv).setOnClickListener(view -> {
            finish();
        });

        recyclerView.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        notificationDataArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alertsAdapter = new AlertsAdapter(notificationDataArrayList);
        recyclerView.setAdapter(alertsAdapter);

        fetchAllNotificationsDB();
    }

    private void updateUI() {
        runOnUiThread(() -> {
            if(notificationDataArrayList.size() < 1){
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                return;
            }

            progressBar.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            alertsAdapter.setNotificationDataArrayList(notificationDataArrayList);
            alertsAdapter.notifyDataSetChanged();
        });
    }


    public void fetchAllNotificationsDB(){
        new NotificationDataFetchAsyncTask(this).execute();
    }

    private class NotificationDataFetchAsyncTask extends AsyncTask<Void, Void, Integer> {

        SimpleDateFormat formatter1;
        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private int value;
        private String TAG = "mCustomAsync";

        NotificationDataFetchAsyncTask(Activity activity) {
            this.weakActivity = new WeakReference<>(activity);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            notificationDataArrayList = (ArrayList<NotificationData>) Common.instance.db.notificationDataDao().getAllNotifications();
            return Common.instance.db.notificationDataDao().getAllNotifications().size();
        }

        @Override
        protected void onPostExecute(Integer feedCount) {
            Log.d(TAG, "onPostExecute: notificationsFetchedCount:" + feedCount);
            updateUI();
        }
    }

}
