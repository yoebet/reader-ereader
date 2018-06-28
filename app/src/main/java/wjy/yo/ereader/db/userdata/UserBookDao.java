package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserBook;

@Dao
public interface UserBookDao extends BaseDao<UserBook> {

    @Query("SELECT * FROM user_book WHERE userName = :userName")
    Single<List<UserBook>> loadUserBooks(String userName);

    @Query("SELECT * FROM user_book WHERE userName = :userName and bookId = :bookId")
    Maybe<UserBook> load(String userName, String bookId);
}
