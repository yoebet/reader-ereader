package wjy.yo.ereader.service;

import wjy.yo.ereader.model.UserInfo;

import wjy.yo.ereader.model.OpResult;

public interface AccountService {

    void getUserInfo(ServiceCallback<UserInfo> callback);

    void login(String name, String pass, ServiceCallback<OpResult> callback);

    void logout(ServiceCallback<OpResult> callback);
}
