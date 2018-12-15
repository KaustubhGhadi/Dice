package in.programmeraki.glaty;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import in.programmeraki.glaty.adapter.HistoryViewPagerAdapter;
import in.programmeraki.glaty.roomdb.AppDatabase;

public class HistoryActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_textview,null);
            tabLayout.getTabAt(i).setCustomView(tv);
        }

        findViewById(R.id.back_iv).setOnClickListener(view -> {
            finish();
        });
    }

    private void setUpAppDatabase() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
    }

    private void setupViewPager(ViewPager viewPager) {
        HistoryViewPagerAdapter adapter = new HistoryViewPagerAdapter(getSupportFragmentManager());

        HistoryFragment todayFragment = new HistoryFragment();
        HistoryFragment lastWeekFragment = new HistoryFragment();
        lastWeekFragment.isLastWeekType = true;

        adapter.addFragment(todayFragment, "Today");
        adapter.addFragment(lastWeekFragment, "Last Week");
        viewPager.setAdapter(adapter);
    }
}
