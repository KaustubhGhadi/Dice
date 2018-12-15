package in.programmeraki.glaty;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ScheduleActivity extends AppCompatActivity {

    private Activity activity;
    private TextView area_tv, date_tv, time_tv, title_sm, title_lg, subtitle;
    private ImageView area_iv;
    private View sep_v;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        area_tv = findViewById(R.id.area_tv);
        date_tv = findViewById(R.id.date_tv);
        time_tv = findViewById(R.id.time_tv);
        area_iv = findViewById(R.id.area_iv);
        title_sm = findViewById(R.id.title_sm);
        title_lg = findViewById(R.id.title_lg);
        subtitle = findViewById(R.id.subtitle);
        sep_v = findViewById(R.id.sep_v);
        if(Common.instance.notificationFeed != null){

            title_lg.setVisibility(View.GONE);

            area_tv.setText(Common.instance.getAreaName(Common.instance.notificationFeed.getArea()));
            area_iv.setImageResource(Common.instance.getAreaImageId(Common.instance.notificationFeed.getArea()));
            sep_v.setBackgroundColor(Common.instance.getAreaColor(Common.instance.notificationFeed.getArea()));
            date_tv.setText(Common.instance.getDateStr(Common.instance.notificationFeed.getC()));
            time_tv.setText(Common.instance.getTimeStr(Common.instance.notificationFeed.getC()));
        }
        activity = this;

        findViewById(R.id.back_iv).setOnClickListener(view -> {
            finish();
        });

    }
}
