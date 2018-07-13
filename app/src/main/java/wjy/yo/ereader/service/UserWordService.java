package wjy.yo.ereader.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.vo.GroupedUserWords;
import wjy.yo.ereader.vo.OperationResult;
import wjy.yo.ereader.vo.VocabularyFilter;

public interface UserWordService {

    Single<List<UserWord>> getAll();

    Single<Map<String, UserWord>> getWordsMap();

    Single<List<GroupedUserWords>> filterAndGroup(VocabularyFilter filter);

    Maybe<UserWord> getOne(String word);

    Single<OperationResult> add(UserWord userWord);

    Single<OperationResult> update(String word, int familiarity);

    Single<OperationResult> remove(String word);
}
