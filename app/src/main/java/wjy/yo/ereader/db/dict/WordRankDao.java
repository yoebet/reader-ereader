package wjy.yo.ereader.db.dict;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.dict.WordRank;

@Dao
public interface WordRankDao extends BaseDao<WordRank> {

    @Query("DELETE FROM dict_word_rank WHERE word = :word")
    int deleteRanks(String word);
}
