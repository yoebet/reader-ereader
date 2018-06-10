package wjy.yo.ereader.db.book;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entityvo.IdVersion;
import wjy.yo.ereader.entity.book.Para;

@Dao
public interface ParaDao extends BaseDao<Para> {

    @Query("DELETE FROM book_para WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM book_para WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,_version FROM book_para WHERE _id = :id")
    IdVersion loadIdVersion(String id);

    @Query("SELECT _id,_version FROM book_para WHERE chapId = :chapId ORDER BY `no`")
    List<IdVersion> loadIdVersions(String chapId);

    @Query("SELECT * FROM book_para WHERE chapId = :chapId ORDER BY `no`")
    LiveData<List<Para>> loadParas(String chapId);

    @Query("DELETE FROM book_para WHERE chapId = :chapId")
    int deleteChapParas(String chapId);

//    @Query("SELECT * FROM para WHERE _id = :id")
//    LiveData<Para> load(String id);

}
