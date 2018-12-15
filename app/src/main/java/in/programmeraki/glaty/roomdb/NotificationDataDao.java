package in.programmeraki.glaty.roomdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NotificationDataDao {

    @Query("SELECT * FROM NotificationData")
    List<NotificationData> getAllNotifications();

    @Insert
    void insertAll(NotificationData... notifications);

    @Delete
    void delete(NotificationData notification);

    @Query("DELETE FROM NotificationData")
    void deleteAllNotifications();
}
