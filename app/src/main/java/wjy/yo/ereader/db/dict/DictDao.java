package wjy.yo.ereader.db.dict;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.dict.Dict;
import wjy.yo.ereader.entityvo.IdVersion;
import wjy.yo.ereader.entityvo.dict.DictEntry;

@Dao
public interface DictDao extends BaseDao<Dict> {

    @Query("DELETE FROM Dict WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM Dict WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,version FROM Dict WHERE _id = :id")
    IdVersion loadIdVersion(String id);


//    @Query("SELECT * FROM Dict WHERE _id = :id")
//    Flowable<Dict> loadBasic(String id);

//    @Query("SELECT * FROM Dict WHERE _id = :id")
//    @Transaction
//    Flowable<DictEntry> load(String id);
}
