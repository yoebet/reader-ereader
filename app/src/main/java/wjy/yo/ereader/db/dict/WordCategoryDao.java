package wjy.yo.ereader.db.dict;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.dict.WordCategory;

@Dao
public interface WordCategoryDao extends BaseDao<WordCategory> {

    @Query("DELETE FROM dict_word_category")
    int deleteAll();

    @Query("SELECT * FROM dict_word_category ORDER BY `no`")
    Single<List<WordCategory>> loadAll();

}
