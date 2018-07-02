package wjy.yo.ereader.db.book;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import io.reactivex.Maybe;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.book.Para;

@Dao
public interface ParaDao extends BaseDao<Para> {

    @Query("DELETE FROM book_para WHERE chapId = :chapId")
    int deleteChapParas(String chapId);

    @Query("SELECT * FROM book_para WHERE id = :id")
    Maybe<Para> load(String id);

}
