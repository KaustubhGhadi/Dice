package in.programmeraki.glaty.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import in.programmeraki.glaty.Common;
import in.programmeraki.glaty.R;
import in.programmeraki.glaty.model.CalenderFeed;

public class CalendarFeedAdapter extends RecyclerView.Adapter<CalendarFeedAdapter.MyViewHolder>{

    private ArrayList<CalenderFeed> feedArr;

    public CalendarFeedAdapter(ArrayList<CalenderFeed> type) {
        this.feedArr = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_calendar_feed, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CalenderFeed feed = feedArr.get(position);
        holder.sep_v.setBackgroundColor(holder.itemView.getContext()
                .getResources().getColor(Common.instance.getAreaColor(feed.getArea())));
        holder.area_tv.setText(Common.instance.getAreaName(feed.getArea()));

        holder.date_tv.setText(Common.instance.getDateStr(feed.getC()));

        holder.time_tv.setText(Common.instance.getTimeStr(feed.getC()));
        holder.area_iv.setImageResource(Common.instance.getAreaImageId(feed.getArea()));
    }

    @Override
    public int getItemCount() {
        return feedArr.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date_tv, time_tv, area_tv;
        ImageView area_iv;
        View sep_v;

        MyViewHolder(View itemView) {
            super(itemView);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            area_tv = itemView.findViewById(R.id.area_tv);
            area_iv = itemView.findViewById(R.id.area_iv);
            sep_v = itemView.findViewById(R.id.sep_v);
        }
    }

    public void setFeedArr(ArrayList<CalenderFeed> feedArr) {
        this.feedArr = feedArr;
    }
}
