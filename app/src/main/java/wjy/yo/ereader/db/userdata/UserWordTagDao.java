package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserWordTag;

@Dao
public interface UserWordTagDao extends BaseDao<UserWordTag> {

    @Query("SELECT * FROM user_word_tag WHERE userName = :userName")
    Flowable<List<UserWordTag>> loadUserWordTags(String userName);
}
