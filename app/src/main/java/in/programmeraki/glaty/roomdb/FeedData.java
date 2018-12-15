package in.programmeraki.glaty.roomdb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FeedData {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "feed_val")
    private String feed_val;

    @ColumnInfo(name = "feed_type")
    private String feed_type;

    @ColumnInfo(name = "pulse")
    private String pulse;

    @ColumnInfo(name = "temp")
    private String temp;

    @ColumnInfo(name = "raw_data")
    private String raw_data;

    @ColumnInfo(name = "date_time_in_millis")
    private long date_time_in_millis;

    public FeedData(String timestamp, String feed_val, String feed_type, String pulse, String temp, String raw_data, long date_time_in_millis) {
        this.timestamp = timestamp;
        this.feed_val = feed_val;
        this.feed_type = feed_type;
        this.pulse = pulse;
        this.temp = temp;
        this.raw_data = raw_data;
        this.date_time_in_millis = date_time_in_millis;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFeed_val() {
        return feed_val;
    }

    public void setFeed_val(String feed_val) {
        this.feed_val = feed_val;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getRaw_data() {
        return raw_data;
    }

    public void setRaw_data(String raw_data) {
        this.raw_data = raw_data;
    }

    public long getDate_time_in_millis() {
        return date_time_in_millis;
    }

    public void setDate_time_in_millis(long date_time_in_millis) {
        this.date_time_in_millis = date_time_in_millis;
    }
}
