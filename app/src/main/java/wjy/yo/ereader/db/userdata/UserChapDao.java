package wjy.yo.ereader.db.userdata;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserChap;

@Dao
public interface UserChapDao extends BaseDao<UserChap> {

    @Query("SELECT * FROM user_chap WHERE userName = :userName")
    LiveData<List<UserChap>> loadUserChaps(String userName);
}
