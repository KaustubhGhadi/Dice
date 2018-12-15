package in.programmeraki.glaty.roomdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {FeedData.class, NotificationData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedDataDao feedDataDao();
    public abstract NotificationDataDao notificationDataDao();
}
