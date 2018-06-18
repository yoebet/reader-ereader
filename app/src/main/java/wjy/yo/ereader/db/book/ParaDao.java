package wjy.yo.ereader.db.book;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.IdVersion;
import wjy.yo.ereader.entity.book.Para;

@Dao
public interface ParaDao extends BaseDao<Para> {

    @Query("SELECT _id,version FROM book_para WHERE chapId = :chapId ORDER BY `no`")
    List<IdVersion> loadIdVersions(String chapId);

//    @Query("DELETE FROM book_para WHERE chapId = :chapId")
//    int deleteChapParas(String chapId);

//    @Query("SELECT * FROM para WHERE _id = :id")
//    LiveData<Para> load(String id);

}
