package in.programmeraki.glaty.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import in.programmeraki.glaty.Common;
import in.programmeraki.glaty.R;
import in.programmeraki.glaty.utils.Constant;

public class NotificationFragment extends Fragment {
    
    Context activity_context;
    private TextView area_tv, date_tv, time_tv, title_sm, title_lg, subtitle;
    private ImageView area_iv;
    private View sep_v;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity_context = context;
        Log.d("tmp2", "onAttach: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Constant.selected_frag_id = 4;
        Log.d("tmp2", "onResume: ");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        area_tv = view.findViewById(R.id.area_tv);
        date_tv = view.findViewById(R.id.date_tv);
        time_tv = view.findViewById(R.id.time_tv);
        area_iv = view.findViewById(R.id.area_iv);
        title_sm = view.findViewById(R.id.title_sm);
        title_lg = view.findViewById(R.id.title_lg);
        subtitle = view.findViewById(R.id.subtitle);
        sep_v = view.findViewById(R.id.sep_v);
        if(Common.instance.notificationFeed != null){

            title_lg.setVisibility(View.GONE);

            area_tv.setText(Common.instance.getAreaName(Common.instance.notificationFeed.getArea()));
            area_iv.setImageResource(Common.instance.getAreaImageId(Common.instance.notificationFeed.getArea()));
            sep_v.setBackgroundColor(Common.instance.getAreaColor(Common.instance.notificationFeed.getArea()));
            date_tv.setText(Common.instance.getDateStr(Common.instance.notificationFeed.getC()));
            time_tv.setText(Common.instance.getTimeStr(Common.instance.notificationFeed.getC()));
        }
    }
}
