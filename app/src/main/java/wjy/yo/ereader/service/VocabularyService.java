package wjy.yo.ereader.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.vo.VocabularyStatistic;


public interface VocabularyService {

    Maybe<WordCategory> inBaseVocabulary(final String word);

    Flowable<UserVocabularyMap> getUserVocabularyMap();

    Single<VocabularyStatistic> statistic();

    Single<List<String>> evaluateWordRankLabels(List<WordRank> wordRanks);

    interface UserVocabularyMap {
        /**
         * @return String (category code) or UserWord
         */
        Object get(String word);

//        static UserVocabularyMap EmptyMap() {
//            return word -> null;
//        }

        UserVocabularyMap InvalidMap = word -> null;
    }

}
