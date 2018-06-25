package wjy.yo.ereader.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.entity.dict.WordCategory;

public interface WordCategoryService {

    Single<Map<String, WordCategory>> getCategoriesMap();

    Maybe<WordCategory> getWordCategory(String code);

    Single<List<String>> getCategoryAllWords(String code);
}
