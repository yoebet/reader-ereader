package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.entityvo.IdVersion;

@Dao
public interface UserDao extends BaseDao<User> {

    @Query("DELETE FROM user WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM user WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,version FROM user WHERE _id = :id")
    IdVersion loadIdVersion(String id);

//    @Query("SELECT * FROM User LIMIT 1")
//    Flowable<User> getUser();

//    @Query("SELECT * FROM User WHERE name = :name")
//    Maybe<User> getUser(String name);

}
