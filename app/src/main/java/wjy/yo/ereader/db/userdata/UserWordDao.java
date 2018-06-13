package wjy.yo.ereader.db.userdata;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserWord;

@Dao
public interface UserWordDao extends BaseDao<UserWord> {

    @Query("SELECT * FROM user_word WHERE userName = :userName")
    LiveData<List<UserWord>> loadUserWords(String userName);
}
