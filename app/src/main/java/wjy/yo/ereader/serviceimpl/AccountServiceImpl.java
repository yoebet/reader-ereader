package wjy.yo.ereader.serviceimpl;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.UserDao;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.vo.OpResult;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.remote.user.AccountAPI;
import wjy.yo.ereader.vo.UserInfo;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject
    AccountAPI accountAPI;

    UserDao userDao;

    private User currentUser;

    private boolean login;

    private Subject<User> userChangeSubject;

    @Inject
    AccountServiceImpl(DB db) {
        System.out.println("new AccountServiceImpl");

        this.userDao = db.userDao();

        userChangeSubject = PublishSubject.create();
    }

    private void setCurrentUser(User cu) {
        if (cu != null) {
            if (this.currentUser == null
                    || !cu.getName().equals(this.currentUser.getName())) {
                userChangeSubject.onNext(cu);
            }
        }
        this.currentUser = cu;
    }

    public Observable<User> getUserChangeObservable() {
        return userChangeSubject;
    }

    public Flowable<Boolean> checkNeedLogin() {
        if (login) {
            return Flowable.just(false);
        }
        return accountAPI.getUserInfo().map((UserInfo ui) -> {
            if (ui == null) {
                return true;
            }
            login = ui.isLogin();
            if (login) {
                resetCurrentUser(ui);
            }
            return !login;
        });
    }

//    public Flowable<User> getCurrentUser() {
//        if (currentUser != null) {
//            return Flowable.just(currentUser);
//        }
//        User cu=userDao.getCurrentUserEagerly();
//        setCurrentUser(cu);
//        return Flowable.just(currentUser);
//        /*return userDao.getCurrentUser().map((User cu) -> {
//            setCurrentUser(cu);
//            return cu;
//        });*/
//    }

    private void insertCurrentUser(UserInfo userInfo) {
        User cu = new User();
        String name = userInfo.getName();
        String id = UUID.nameUUIDFromBytes(name.getBytes()).toString();
        cu.setId(id);
        cu.setName(name);
        cu.setNickName(userInfo.getNickName());
        cu.setAccessToken(userInfo.getAccessToken());
        cu.setLastLoginAt(new Date());
        cu.setVersion(1);
        cu.setCurrent(true);
        setCurrentUser(cu);
        userDao.insert(cu);
    }

    private boolean updateUser(User cu, UserInfo userInfo) {
        boolean needUpdate = false;
        String at = userInfo.getAccessToken();
        if (at != null && !at.equals(cu.getAccessToken())) {
            cu.setAccessToken(at);
            needUpdate = true;
        }
        String nn = userInfo.getNickName();
        if (nn != null && !nn.equals(cu.getNickName())) {
            cu.setNickName(nn);
            needUpdate = true;
        }
        return needUpdate;
    }

    private void resetCurrentUser(UserInfo userInfo) {
        String name = userInfo.getName();
        if (currentUser != null && name.equals(currentUser.getName())) {
            boolean needUpdate = updateUser(currentUser, userInfo);
            if (needUpdate) {
                userDao.update(currentUser);
            }
            return;
        }
        User cu = userDao.getCurrentUserEagerly();
//        Disposable disp = userDao.getCurrentUser().subscribe((User cu) -> {
        if (cu == null) {
            insertCurrentUser(userInfo);
            return;
        }
        if (name.equals(cu.getName())) {
            boolean needUpdate = updateUser(cu, userInfo);
            if (needUpdate) {
                userDao.update(cu);
            }
            setCurrentUser(cu);
            return;
        }
        cu.setCurrent(false);
        userDao.update(cu);
        User u = userDao.getUser(name);
//            userDao.getUser(name).subscribe((User u) -> {
        if (u == null) {
            insertCurrentUser(userInfo);
            return;
        }
        u.setCurrent(true);
        updateUser(u, userInfo);
        userDao.update(u);
//            });
//        });

    }

    @Override
    public Flowable<UserInfo> login(final String name, String pass) {
        return accountAPI.login(name, pass).map((UserInfo userInfo) -> {
            if (userInfo.isOk()) {
                login = true;
                userInfo.setName(name);
                resetCurrentUser(userInfo);
            } else {
                login = false;
            }
            return userInfo;
        });
    }

    @Override
    public Flowable<OpResult> logout() {
        return accountAPI.logout().map((OpResult opr) -> {
            this.login = false;
            return opr;
        });
    }
}
