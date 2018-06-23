package wjy.yo.ereader.remote;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import wjy.yo.ereader.remotevo.UserPreference;
import wjy.yo.ereader.remotevo.OpResult;

public interface PreferenceAPI {

    @GET("user_preferences/")
    Maybe<UserPreference> get();

    @POST("user_preferences/")
    Single<OpResult> save(@Body UserPreference up);

    @POST("user_preferences/code/{code}")
    Single<OpResult> saveValue(@Path("code") String code, @Field("value") Object value);

}
