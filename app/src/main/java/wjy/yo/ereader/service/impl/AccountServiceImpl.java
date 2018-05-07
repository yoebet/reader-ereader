package wjy.yo.ereader.service.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wjy.yo.ereader.model.Failure;
import wjy.yo.ereader.model.OpResult;
import wjy.yo.ereader.model.UserInfo;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.ServiceCallback;
import wjy.yo.ereader.service.remote.AccountAPI;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject
    AccountAPI accountAPI;

    @Inject
    AccountServiceImpl() {
        System.out.println("new AccountServiceImpl");
    }

    static Callback retrofitCallback(final ServiceCallback callback, final Failure failure) {
        return new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Object o = response.body();
                System.out.println(o);
                callback.onComplete(o);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onFailure(failure);
            }
        };
    }

    @Override
    public void getUserInfo(final ServiceCallback<UserInfo> callback) {
        accountAPI.getUserInfo().enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                callback.onComplete(userInfo);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                callback.onFailure(new Failure("NETWORK", "获取用户信息失败"));
            }
        });
    }

    @Override
    public void login(String name, String pass, final ServiceCallback<OpResult> callback) {
        accountAPI.login(name, pass).enqueue(retrofitCallback(callback, Failure.GENERAL));
    }

    @Override
    public void logout(ServiceCallback<OpResult> callback) {
        accountAPI.logout().enqueue(retrofitCallback(callback, Failure.GENERAL));
    }
}
