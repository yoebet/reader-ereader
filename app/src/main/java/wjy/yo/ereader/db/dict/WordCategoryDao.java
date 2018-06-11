package wjy.yo.ereader.db.dict;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entityvo.IdVersion;

@Dao
public interface WordCategoryDao extends BaseDao<WordCategory> {

    @Query("DELETE FROM dict_word_category WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM dict_word_category WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,version FROM dict_word_category WHERE _id = :id")
    IdVersion loadIdVersion(String id);

//    @Query("SELECT * FROM dict_word_category")
//    Flowable<List<WordCategory>> loadAll();

}
