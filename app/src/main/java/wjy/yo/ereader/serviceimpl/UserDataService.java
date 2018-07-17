package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.UUID;

import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.UserData;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.util.ExceptionHandlers;

abstract class UserDataService {

    protected AccountService accountService;

    protected DataSyncService dataSyncService;

    protected String userName;


    UserDataService(AccountService accountService, DataSyncService dataSyncService) {
        this.accountService = accountService;
        this.dataSyncService = dataSyncService;
    }

    @SuppressLint("CheckResult")
    protected void observeUserChange() {
        accountService.getUserChangeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe((User user) -> {
                    userName = user.getName();
                    System.out.println(this.getClass() + ", User: " + userName);
                    onUserChanged();
                }, ExceptionHandlers::handle);
    }


    protected void onUserChanged() {
    }


    void setupNewLocal(UserData ud) {
        ud.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        ud.setVersion(1);
        ud.setUserName(userName);
        ud.setLocal(true);
        Date now = new Date();
        ud.setCreatedAt(now);
        ud.setUpdatedAt(now);
    }


    void updateLocal(UserData ud) {
        ud.setVersion(ud.getVersion() + 1);
        ud.setLocal(true);
        ud.setUpdatedAt(new Date());
    }
}
