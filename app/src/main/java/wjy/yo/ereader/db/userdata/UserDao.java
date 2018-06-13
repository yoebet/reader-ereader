package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.User;

@Dao
public interface UserDao extends BaseDao<User> {

    @Query("SELECT * FROM User WHERE name = :name")
    Flowable<User> getUser(String name);

}
