package in.programmeraki.glaty.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import in.programmeraki.glaty.Common;
import in.programmeraki.glaty.R;
import in.programmeraki.glaty.roomdb.FeedData;
import in.programmeraki.glaty.utils.Constant;

public class HomeFragment extends Fragment {
    static ArrayList<FeedData> feedDataArrayList;
    static float leftArmShotsCount = 0;
    static float rightArmShotsCount = 0;
    static float leftTheighShotsCount = 0;
    static float rightTheighShotsCount = 0;
    static float leftButtShotsCount = 0;
    static float rightButtShotsCount = 0;
    static float stomachShotsCount = 0;
    Context activity_context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ViewGroup top_head_rl, left_thigh_vg, right_thigh_vg, left_arm_vg, right_arm_vg,
            right_butt_vg, left_butt_vg;
    Button submit_btn;

    boolean isProcessing = false;
    final static String TAG = "mCustom";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity_context = context;
        Log.d("tmp2", "onAttach: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Constant.selected_frag_id = 1;
        Log.d("tmp2", "onResume: ");
        fetchAllFeedFromDB();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        top_head_rl = view.findViewById(R.id.top_head_rl);
        left_arm_vg = view.findViewById(R.id.left_arm_vg);
        right_arm_vg = view.findViewById(R.id.right_arm_vg);
        left_butt_vg = view.findViewById(R.id.left_butt_vg);
        right_butt_vg = view.findViewById(R.id.right_butt_vg);
        left_thigh_vg = view.findViewById(R.id.left_thigh_vg);
        right_thigh_vg = view.findViewById(R.id.right_thigh_vg);
//        stomach_vg = view.findViewById(R.id.stomach_vg);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity_context);
        editor = sharedPreferences.edit();

        String str_name = "Hi " + sharedPreferences.getString(Constant.fname, "Mary") + "!";
        ((TextView)view.findViewById(R.id.title_tv)).setText(str_name);

        fetchAllFeedFromDB();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void updateUI(){
        if(getActivity() == null){
            return;
        }

        getActivity().runOnUiThread(() -> {
            if(left_arm_vg == null){
                return;
            }

            for(int index=0; index < (left_arm_vg).getChildCount(); index++) {
                View nextChild = left_arm_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(leftArmShotsCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (leftArmShotsCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (right_arm_vg).getChildCount(); index++) {
                View nextChild = right_arm_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(rightArmShotsCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (rightArmShotsCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (left_thigh_vg).getChildCount(); index++) {
                View nextChild = left_thigh_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(leftTheighShotsCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (leftTheighShotsCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (right_thigh_vg).getChildCount(); index++) {
                View nextChild = right_thigh_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(rightTheighShotsCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (rightTheighShotsCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (left_butt_vg).getChildCount(); index++) {
                View nextChild = left_butt_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(leftButtShotsCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (leftButtShotsCount/feedDataArrayList.size())*100)
                                    + "%"
                    );
                }
            }

            for(int index=0; index < (right_butt_vg).getChildCount(); index++) {
                View nextChild = right_butt_vg.getChildAt(index);
                if (nextChild.getTag().toString().equalsIgnoreCase("shots_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(rightButtShotsCount + " SHOTS");
                } else if (nextChild.getTag().toString().equalsIgnoreCase("percent_tv")) {
                    TextView tv = (TextView) nextChild;
                    tv.setText(
                            String.format("%.2f", (rightButtShotsCount/feedDataArrayList.size())*100)
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
        if(getContext() == null || getActivity() == null || isProcessing){
            return;
        }
        isProcessing = true;
        new FeedDataFetchAsyncTask(getActivity()).execute();
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

            leftArmShotsCount = 0;
            rightArmShotsCount = 0;
            leftTheighShotsCount = 0;
            rightTheighShotsCount = 0;
            leftButtShotsCount = 0;
            rightButtShotsCount = 0;
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
                    leftArmShotsCount += 1;
                } else if(feedVal == Constant.DICETWO){
                    rightArmShotsCount += 1;
                } else if(feedVal == Constant.DICETHREE){
                    leftTheighShotsCount += 1;
                } else if(feedVal == Constant.DICEFOUR){
                    rightTheighShotsCount += 1;
                } else if(feedVal == Constant.DICEFIVE){
                    leftButtShotsCount += 1;
                } else if(feedVal == Constant.DICESIX){
                    rightButtShotsCount += 1;
                }
            }
            updateUI();
            isProcessing = false;
        }
    }

}
