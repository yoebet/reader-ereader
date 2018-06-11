package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserChap;
import wjy.yo.ereader.entityvo.IdVersion;

@Dao
public interface UserChapDao extends BaseDao<UserChap> {

    @Query("DELETE FROM user_chap WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM user_chap WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,version FROM user_chap WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
