package wjy.yo.ereader.remote.user;

import io.reactivex.Flowable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import wjy.yo.ereader.vo.OpResult;
import wjy.yo.ereader.vo.UserInfo;

public interface AccountAPI {

    @GET("login/userinfo")
    Flowable<UserInfo> getUserInfo();

    @POST("login")
    @FormUrlEncoded
    Flowable<UserInfo> login(@Field("name") String name, @Field("pass") String pass);

    @DELETE("login")
    Flowable<OpResult> logout();
}
