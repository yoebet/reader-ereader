package wjy.yo.ereader.service;

import java.util.Map;

import io.reactivex.Single;
import wjy.yo.ereader.entity.userdata.UserWord;


public interface VocabularyService {

    Map<String, Integer> getMyWordsMap();

    Single<UserVocabularyMap> getUserVocabularyMap();

    interface UserVocabularyMap {
        Object get(String word);

        static UserVocabularyMap EmptyMap() {
            return word -> null;
        }
    }

}
