package wjy.yo.ereader.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.DataSyncRecord;

@Dao
public interface DataSyncRecordDao extends BaseDao<DataSyncRecord> {

    @Query("SELECT * FROM data_sync_record WHERE userName = :userName and category = :category and direction = :direction")
    DataSyncRecord getUserRecord(String userName, String category, String direction);

    @Query("SELECT * FROM data_sync_record WHERE userName is NULL and category = :category and direction = :direction")
    DataSyncRecord getCommonRecord(String category, String direction);
}
