package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.entity.UserData;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;

public abstract class UserDataService {

    protected AccountService accountService;

    protected DataSyncService dataSyncService;

    protected String userName;

    protected boolean offline = false;


    @SuppressLint("CheckResult")
    public UserDataService(AccountService accountService, DataSyncService dataSyncService) {
        this.accountService = accountService;
        this.dataSyncService = dataSyncService;
        accountService.getUserChangeObservable().subscribe((User user) -> {
            userName = user.getName();
            System.out.println(this.getClass() + ", User: " + userName);
            onUserChanged();
        });
    }


    protected void onUserChanged() {
    }


    protected void setupNewUserData(UserData ud) {
        ud.setId(UUID.randomUUID().toString());
        ud.setVersion(1);
        ud.setLocal(true);
        ud.setUserName(userName);
        ud.setUpdatedAt(new Date());
    }

}
