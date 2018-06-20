package wjy.yo.ereader.remote;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.vo.OpResult;

public interface UserWordAPI {

    @GET("user_words/")
    Single<List<UserWord>> getAll();

    @POST("user_words/")
    Single<OpResult> addAWord(@Body UserWord userWord);

    @PUT("user_words/{word}")
    @FormUrlEncoded
    Single<OpResult> updateAWord(@Path("word") String word, @Field("pass") int familiarity);

    @DELETE("user_words/{word}")
    Single<OpResult> removeAWord(@Path("word") String word);

}
