package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.Preference;

@Dao
public interface PreferenceDao extends BaseDao<Preference> {

    @Query("SELECT * FROM user_preference WHERE userName = :userName")
    Flowable<List<Preference>> loadUserPreferences(String userName);
}
