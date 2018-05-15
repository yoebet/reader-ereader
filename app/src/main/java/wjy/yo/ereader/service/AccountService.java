package wjy.yo.ereader.service;

import wjy.yo.ereader.vo.UserInfo;

import wjy.yo.ereader.vo.OpResult;

public interface AccountService {

    UserInfo getUserInfo();

    void getUserInfo(ServiceCallback<UserInfo> callback);

    void login(String name, String pass, ServiceCallback<OpResult> callback);

    void logout(ServiceCallback<OpResult> callback);
}
