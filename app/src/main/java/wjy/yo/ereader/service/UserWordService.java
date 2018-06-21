package wjy.yo.ereader.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.entity.userdata.UserWord;

public interface UserWordService {

    Single<List<UserWord>> getAll();

    Single<Map<String, UserWord>> getWordsMap();

    Maybe<UserWord> getOne(String word);

    void add(UserWord userWord);

    void update(String word, int familiarity);

    void remove(String word);
}
