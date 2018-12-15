package in.programmeraki.glaty.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.programmeraki.glaty.R;
import in.programmeraki.glaty.roomdb.NotificationData;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder>{

    private ArrayList<NotificationData> notificationDataArrayList;

    public AlertsAdapter(ArrayList<NotificationData> notificationDataArrayList) {
        this.notificationDataArrayList = notificationDataArrayList;
    }

    public void setNotificationDataArrayList(ArrayList<NotificationData> notificationDataArrayList) {
        this.notificationDataArrayList = notificationDataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_alerts, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NotificationData notificationData;
        notificationData = notificationDataArrayList.get(position);

        holder.message_tv.setText(notificationData.getMessage());
        holder.title_tv.setText(notificationData.getTitle());
        holder.datetime_tv.setText(notificationData.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notificationDataArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title_tv, datetime_tv, message_tv;

        MyViewHolder(View itemView) {
            super(itemView);
            message_tv = itemView.findViewById(R.id.message_tv);
            datetime_tv = itemView.findViewById(R.id.datetime_tv);
            title_tv = itemView.findViewById(R.id.title_tv);
        }
    }
}
