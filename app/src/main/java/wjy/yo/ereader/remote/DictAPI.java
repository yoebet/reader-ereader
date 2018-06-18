package wjy.yo.ereader.remote;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entityvo.dict.PosComplete;

public interface DictAPI {

    @GET("dict/{idOrWord}")
    Flowable<Map> dictLookup(@Path("idOrWord") String idOrWord);

    @GET("dict/{word}/complete")
    Flowable<List<PosComplete>> getCompleteMeanings(@Path("word") String word);

    @GET("dict/loadBaseForms")
    Flowable<List<String[]>> loadBaseForms();


    @GET("word_categories/")
    Flowable<List<WordCategory>> getAllCategories();

    @GET("word_categories/{code}/loadAll")
    Flowable<List<String>> getCategoryAllWords(@Path("code") String code);
}
