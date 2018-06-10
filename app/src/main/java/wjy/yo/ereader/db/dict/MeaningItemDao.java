package wjy.yo.ereader.db.dict;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entityvo.IdVersion;

@Dao
public interface MeaningItemDao extends BaseDao<MeaningItem> {

    @Query("DELETE FROM dict_meaning_item WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM dict_meaning_item WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,_version FROM dict_meaning_item WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
