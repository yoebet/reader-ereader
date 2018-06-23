package wjy.yo.ereader.service;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.remotevo.OpResult;
import wjy.yo.ereader.remotevo.UserInfo;

public interface AccountService {

    boolean isLogin();

    Flowable<Boolean> checkNeedLogin();

    Flowable<UserInfo> login(String name, String pass);

    Flowable<OpResult> logout();

    Observable<User> getUserChangeObservable();
}
