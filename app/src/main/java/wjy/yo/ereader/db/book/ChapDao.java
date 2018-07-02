package wjy.yo.ereader.db.book;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import io.reactivex.Maybe;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.ChapDetail;

@Dao
public interface ChapDao extends BaseDao<Chap> {

    @Query("DELETE FROM book_chap WHERE bookId = :bookId")
    int deleteBookChaps(String bookId);

    @Query("SELECT * FROM book_chap WHERE id = :id")
    Maybe<Chap> load(String id);

    @Transaction
    @Query("SELECT * FROM book_chap WHERE id = :id")
    Maybe<ChapDetail> loadDetail(String id);
}
