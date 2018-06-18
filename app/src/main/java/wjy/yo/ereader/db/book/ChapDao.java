package wjy.yo.ereader.db.book;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.IdVersion;
import wjy.yo.ereader.entityvo.book.ChapDetail;

@Dao
public interface ChapDao extends BaseDao<Chap> {

    @Query("SELECT _id,version FROM book_chap WHERE _id = :id")
    IdVersion loadIdVersion(String id);

//    @Query("DELETE FROM book_chap WHERE bookId = :bookId")
//    int deleteBookChaps(String bookId);

    @Query("SELECT * FROM book_chap WHERE _id = :id")
    Flowable<Chap> load(String id);

    @Transaction
    @Query("SELECT * FROM book_chap WHERE _id = :id")
    Flowable<ChapDetail> loadDetail(String id);
}
