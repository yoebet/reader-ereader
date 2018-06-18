package wjy.yo.ereader.service;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.vo.OpResult;
import wjy.yo.ereader.vo.UserInfo;

public interface AccountService {

    Flowable<Boolean> checkNeedLogin();

//    Flowable<User> getCurrentUser();

    Flowable<UserInfo> login(String name, String pass);

    Flowable<OpResult> logout();

    Observable<User> getUserChangeObservable();
}
