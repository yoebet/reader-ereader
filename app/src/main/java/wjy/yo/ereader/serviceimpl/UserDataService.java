package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.UUID;

import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.UserData;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;

abstract class UserDataService {

    protected AccountService accountService;

    protected DataSyncService dataSyncService;

    protected String userName;


    UserDataService(AccountService accountService, DataSyncService dataSyncService) {
        this.accountService = accountService;
        this.dataSyncService = dataSyncService;
    }

    @SuppressLint("CheckResult")
    protected void observeUserChange(){
        accountService.getUserChangeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe((User user) -> {
                    userName = user.getName();
                    System.out.println(this.getClass() + ", User: " + userName);
                    onUserChanged();
                });
    }


    protected void onUserChanged() {
    }


    void setupNewUserData(UserData ud) {
        ud.setId(UUID.randomUUID().toString());
        ud.setVersion(1);
        ud.setLocal(true);
        ud.setUserName(userName);
        ud.setUpdatedAt(new Date());
    }


    void updateUserData(UserData ud) {
        ud.setLocal(true);
        ud.setVersion(ud.getVersion() + 1);
        ud.setUpdatedAt(new Date());
    }
}
