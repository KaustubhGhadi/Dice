package in.programmeraki.glaty.roomdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FeedDataDao {

    @Query("SELECT * FROM FeedData")
    List<FeedData> getAllFeedData();

    @Query("SELECT * FROM FeedData WHERE uid IN (:feedIds)")
    List<FeedData> loadAllByIds(int[] feedIds);

    @Query("SELECT * FROM FeedData WHERE date_time_in_millis IS (:millis)")
    List<FeedData> loadAllByCreatedAt(long millis);

    @Insert
    void insertAll(FeedData... feeds);

    @Delete
    void delete(FeedData feed);

    @Query("DELETE FROM FeedData")
    void deleteAllFeedData();
}
