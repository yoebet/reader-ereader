package wjy.yo.ereader.service.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wjy.yo.ereader.service.vo.Failure;
import wjy.yo.ereader.service.vo.OpResult;
import wjy.yo.ereader.service.vo.UserInfo;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.ServiceCallback;
import wjy.yo.ereader.remote.AccountAPI;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject
    AccountAPI accountAPI;

    private UserInfo userInfo;

    @Inject
    AccountServiceImpl() {
        System.out.println("new AccountServiceImpl");
    }

    static <M> Callback<M> retrofitCallback(final ServiceCallback<M> callback, final Failure failure) {
        return new Callback<M>() {
            @Override
            public void onResponse(Call<M> call, Response<M> response) {
                M o = response.body();
                System.out.println(o);
                callback.onComplete(o);
            }

            @Override
            public void onFailure(Call<M> call, Throwable t) {
                callback.onFailure(failure);
            }
        };
    }

    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void getUserInfo(final ServiceCallback<UserInfo> callback) {
        accountAPI.getUserInfo().enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo ui = response.body();
                callback.onComplete(ui);
                userInfo = ui;
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                callback.onFailure(new Failure("NETWORK", "获取用户信息失败"));
            }
        });
    }

    @Override
    public void login(final String name, String pass, final ServiceCallback<OpResult> callback) {
        accountAPI.login(name, pass).enqueue(new Callback<OpResult>() {
            @Override
            public void onResponse(Call<OpResult> call, Response<OpResult> response) {
                OpResult result = response.body();
                System.out.println(result);
                userInfo = new UserInfo();
                userInfo.setLogin(true);
                userInfo.setName(name);
                callback.onComplete(result);
            }

            @Override
            public void onFailure(Call<OpResult> call, Throwable t) {
                callback.onFailure(Failure.GENERAL);
            }
        });
    }

    @Override
    public void logout(final ServiceCallback<OpResult> callback) {
        accountAPI.logout().enqueue(new Callback<OpResult>() {
            @Override
            public void onResponse(Call<OpResult> call, Response<OpResult> response) {
                OpResult result = response.body();
                System.out.println(result);
                userInfo = null;
                callback.onComplete(result);
            }

            @Override
            public void onFailure(Call<OpResult> call, Throwable t) {
                callback.onFailure(Failure.GENERAL);
            }
        });
    }
}
