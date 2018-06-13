package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserBook;

@Dao
public interface UserBookDao extends BaseDao<UserBook> {

    @Query("SELECT * FROM user_book WHERE userName = :userName")
    Flowable<List<UserBook>> loadUserBooks(String userName);
}
