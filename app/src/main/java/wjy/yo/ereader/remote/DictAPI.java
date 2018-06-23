package wjy.yo.ereader.remote;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.remotevo.PosComplete;

public interface DictAPI {

    @GET("dict/{idOrWord}")
    Maybe<DictEntry> dictLookup(@Path("idOrWord") String idOrWord);

    @GET("dict/{word}/complete")
    Single<List<PosComplete>> getCompleteMeanings(@Path("word") String word);

    @POST("dict/loadBaseForms")
    Single<List<String[]>> loadBaseForms();


    @GET("word_categories/")
    Single<List<WordCategory>> getAllCategories();

    @POST("word_categories/{code}/loadAll")
    Single<List<String>> loadCategoryAllWords(@Path("code") String code);
}
