package wjy.yo.ereader.db.userdata;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserWordTag;

@Dao
public interface UserWordTagDao extends BaseDao<UserWordTag> {

    @Query("SELECT * FROM user_word_tag WHERE userName = :userName")
    LiveData<List<UserWordTag>> loadUserWordTags(String userName);
}
