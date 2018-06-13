package wjy.yo.ereader.db.dict;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.dict.WordCategory;

@Dao
public interface WordCategoryDao extends BaseDao<WordCategory> {

    @Query("SELECT * FROM dict_word_category")
    LiveData<List<WordCategory>> loadAll();

}
