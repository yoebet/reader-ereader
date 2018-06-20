package wjy.yo.ereader.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.LocalSetting;

@Dao
public interface LocalSettingDao extends BaseDao<LocalSetting> {

    @Query("SELECT * FROM setting")
    Flowable<List<LocalSetting>> loadAll();
}
