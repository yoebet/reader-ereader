package wjy.yo.ereader.db.userdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.userdata.UserChap;

@Dao
public interface UserChapDao extends BaseDao<UserChap> {

    @Query("SELECT * FROM user_chap WHERE userName = :userName and bookId = :bookId")
    Single<List<UserChap>> loadChaps(String userName, String bookId);

    @Query("DELETE FROM user_chap WHERE userName = :userName and bookId = :bookId")
    void deleteChaps(String userName, String bookId);
}
