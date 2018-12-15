package in.programmeraki.glaty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import in.programmeraki.glaty.fragment.HomeFragment;
import in.programmeraki.glaty.roomdb.FeedData;
import in.programmeraki.glaty.utils.Constant;

import static java.security.AccessController.getContext;

public class Statistics extends Activity {
    static ArrayList<FeedData> feedDataArrayList;
    static float twoCount = 0;
    static float oneCount = 0;
    static float threeCount = 0;
    static float fourCount = 0;
    static float fiveCount = 0;
    static float sixCount = 0;
    static float stomachShotsCount = 0;
    Context activity_context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ViewGroup top_head_rl, one_vg, two_vg, three_vg, four_vg, five_vg, six_vg;
    Button submit_btn;

    boolean isProcessing = false;
    final static String TAG = "mCustom";

    @Override
    public void onResume() {
        super.onResume();
        Constant.selected_frag_id = 1;
        Log.d("tmp2", "onResume: ");
        fetchAllFeedFromDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        top_head_rl = findViewById(R.id.top_head_rl);
        one_vg = findViewById(R.id.one_vg);
        two_vg = findViewById(R.id.two_vg);
        three_vg = findViewById(R.id.three_vg);
        four_vg = findViewById(R.id.four_vg);
        five_vg = findViewById(R.id.five_vg);
        six_vg = findViewById(R.id.six_vg);
//        stomach_vg = view.findViewById(R.id.stomach_vg);

//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity_context);
//        editor = sharedPreferences.edit();
//
//        String str_name = "Hi " + sharedPreferences.getString(Constant.fname, "Mary") + "!";
//        ((TextView)findViewById(R.id.title_tv)).setText(str_name);

        findViewById(R.id.back_iv).setOnClickListener(view -> {
            finish();
        });

        fetchAllFeedFromDB();
    }


    public void updateUI(){
        if(Statistics.this == null){
            return;
        }

        Statistics.this.runOnUiThread(() -> {
            if(one_vg == null){
                return;
            }

            for(int index=0; index < (one_vg).getChildCount(); index++) {
                View nextChild = one_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(oneCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (oneCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }


            for(int index=0; index < (two_vg).getChildCount(); index++) {
                View nextChild = two_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(twoCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (twoCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (three_vg).getChildCount(); index++) {
                View nextChild = three_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(threeCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (threeCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (four_vg).getChildCount(); index++) {
                View nextChild = four_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(fourCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (fourCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (five_vg).getChildCount(); index++) {
                View nextChild = five_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(fiveCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (fiveCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (six_vg).getChildCount(); index++) {
                View nextChild = six_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(sixCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (sixCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

//            for(int index=0; index < (stomach_vg).getChildCount(); index++) {
//                View nextChild = stomach_vg.getChildAt(index);
//                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
//                    TextView tv = (TextView) nextChild;
//                    tv.setText(stomachShotsCount + " SHOTS");
//                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
//                    TextView tv = (TextView) nextChild;
//                    tv.setText(
//                            String.format("%.2f", (stomachShotsCount/feedDataArrayList.size())*100)
//                                    + "%"
//                    );
//                }
//            }
        });
    }

    public void fetchAllFeedFromDB(){
        Log.d(TAG, "fetchAllFeedFromDB: updating values according to new injection");
        if(getContext() == null || Statistics.this == null || isProcessing){
            return;
        }
        isProcessing = true;
        new FeedDataFetchAsyncTask(Statistics.this).execute();
    }

    private class FeedDataFetchAsyncTask extends AsyncTask<Void, Void, Integer> {

        SimpleDateFormat formatter1;
        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private int value;
        private String TAG = "mCustomAsync";

        FeedDataFetchAsyncTask(Activity activity) {
            this.weakActivity = new WeakReference<>(activity);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            oneCount = 0;
            twoCount = 0;
            threeCount = 0;
            fourCount = 0;
            fiveCount = 0;
            sixCount = 0;
//            stomachShotsCount = 0;
            feedDataArrayList = (ArrayList<FeedData>) Common.instance.db.feedDataDao().getAllFeedData();
            return Common.instance.db.feedDataDao().getAllFeedData().size();
        }

        @Override
        protected void onPostExecute(Integer feedCount) {
            Log.d(TAG, "onPostExecute: feedsFetchedCount:" + feedCount);
            for (FeedData feed: feedDataArrayList) {
                int feedVal = Common.instance.getFeedAreaType(Integer.parseInt(feed.getFeed_val()));

//                if(feedVal == Constant.AREA_LEFT_ARM){
//                    leftArmShotsCount += 1;
//                } else if(feedVal == Constant.AREA_RIGHT_ARM){
//                    rightArmShotsCount += 1;
//                } else if(feedVal == Constant.AREA_LEFT_THIGH){
//                    leftTheighShotsCount += 1;
//                } else if(feedVal == Constant.AREA_RIGHT_THIGH){
//                    rightTheighShotsCount += 1;
//                } else if(feedVal == Constant.AREA_LEFT_BUTT){
//                    leftButtShotsCount += 1;
//                } else if(feedVal == Constant.AREA_RIGHT_BUTT){
//                    rightButtShotsCount += 1;
//                } else if(feedVal == Constant.AREA_STOMACH) {
//                    stomachShotsCount += 1;
//                }

                if(feedVal == Constant.DICEONE){
                    oneCount += 1;
                } else if(feedVal == Constant.DICETWO){
                    twoCount += 1;
                } else if(feedVal == Constant.DICETHREE){
                    threeCount += 1;
                } else if(feedVal == Constant.DICEFOUR){
                    fourCount += 1;
                } else if(feedVal == Constant.DICEFIVE){
                    fiveCount += 1;
                } else if(feedVal == Constant.DICESIX){
                    sixCount += 1;
                }
            }
            updateUI();
            isProcessing = false;
        }
    }

}
