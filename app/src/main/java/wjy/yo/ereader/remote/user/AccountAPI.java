package wjy.yo.ereader.remote.user;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import wjy.yo.ereader.model.OpResult;
import wjy.yo.ereader.model.UserInfo;

public interface AccountAPI {

    @GET("login/userinfo")
    Call<UserInfo> getUserInfo();

    @POST("login")
    @FormUrlEncoded
    Call<OpResult> login(@Field("name") String name, @Field("pass") String pass);

    @DELETE("login")
    Call<OpResult> logout();
}
