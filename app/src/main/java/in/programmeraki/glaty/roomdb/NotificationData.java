package in.programmeraki.glaty.roomdb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class NotificationData {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "date_time_in_millis")
    private long date_time_in_millis;

    public NotificationData(String timestamp, String title, String message, long date_time_in_millis) {
        this.timestamp = timestamp;
        this.title = title;
        this.message = message;
        this.date_time_in_millis = date_time_in_millis;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate_time_in_millis() {
        return date_time_in_millis;
    }

    public void setDate_time_in_millis(long date_time_in_millis) {
        this.date_time_in_millis = date_time_in_millis;
    }
}
