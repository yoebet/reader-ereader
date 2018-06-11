package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserWordTag;
import wjy.yo.ereader.entityvo.IdVersion;

@Dao
public interface UserWordTagDao extends BaseDao<UserWordTag> {

    @Query("DELETE FROM user_word_tag WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM user_word_tag WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,version FROM user_word_tag WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
