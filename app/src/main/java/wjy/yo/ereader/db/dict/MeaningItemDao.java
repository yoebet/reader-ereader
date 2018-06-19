package wjy.yo.ereader.db.dict;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.dict.MeaningItem;

@Dao
public interface MeaningItemDao extends BaseDao<MeaningItem> {

    @Query("DELETE FROM dict_meaning_item WHERE word = :word")
    int deleteMeaningItems(String word);

}
