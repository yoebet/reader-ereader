package wjy.yo.ereader.db.book;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.book.Para;

@Dao
public interface ParaDao extends BaseDao<Para> {

    @Query("DELETE FROM book_para WHERE chapId = :chapId")
    int deleteChapParas(String chapId);

//    @Query("SELECT * FROM para WHERE id = :id")
//    LiveData<Para> load(String id);

}
