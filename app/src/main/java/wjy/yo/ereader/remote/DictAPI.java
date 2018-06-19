package wjy.yo.ereader.remote;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.entityvo.dict.PosComplete;

public interface DictAPI {

    @GET("dict/{idOrWord}")
    Maybe<DictEntry> dictLookup(@Path("idOrWord") String idOrWord);

    @GET("dict/{word}/complete")
    Maybe<List<PosComplete>> getCompleteMeanings(@Path("word") String word);

    @GET("dict/loadBaseForms")
    Maybe<List<String[]>> loadBaseForms();


    @GET("word_categories/")
    Maybe<List<WordCategory>> getAllCategories();

    @GET("word_categories/{code}/loadAll")
    Maybe<List<String>> getCategoryAllWords(@Path("code") String code);
}
