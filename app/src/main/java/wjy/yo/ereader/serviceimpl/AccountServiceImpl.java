package wjy.yo.ereader.serviceimpl;

import android.arch.lifecycle.LiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.vo.Failure;
import wjy.yo.ereader.vo.OpResult;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.ServiceCallback;
import wjy.yo.ereader.remote.user.AccountAPI;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject
    AccountAPI accountAPI;

    private User currentUser;

    private boolean login;

    @Inject
    AccountServiceImpl() {
        System.out.println("new AccountServiceImpl");
    }


    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public LiveData<User> getCurrentUser() {
//        if (currentUser != null) {
//            return new LiveData<User>() {
//                @Override
//                protected void onActive() {
//                    super.onActive();
//                    postValue(currentUser);
//                }
//            };
//        }
        return null;
    }

    @Override
    public void login(final String name, String pass, final ServiceCallback<OpResult> callback) {
        accountAPI.login(name, pass).enqueue(new Callback<OpResult>() {
            @Override
            public void onResponse(Call<OpResult> call, Response<OpResult> response) {
                OpResult result = response.body();
                System.out.println(result);


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

                callback.onComplete(result);
            }

            @Override
            public void onFailure(Call<OpResult> call, Throwable t) {
                callback.onFailure(Failure.GENERAL);
            }
        });
    }
}
