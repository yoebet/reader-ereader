package wjy.yo.ereader.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import wjy.yo.ereader.entity.userdata.UserWord;

public interface UserWordService {

    Flowable<List<UserWord>> getAll();

    Flowable<Map<String, UserWord>> getWordsMap();

    Maybe<UserWord> getOne(String word);

    void add(UserWord userWord);

    void update(String word, int familiarity);

    void remove(String word);
}
