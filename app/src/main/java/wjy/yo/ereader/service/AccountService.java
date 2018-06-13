package wjy.yo.ereader.service;

import wjy.yo.ereader.vo.OpResult;

public interface AccountService {

    void login(String name, String pass, ServiceCallback<OpResult> callback);

    void logout(ServiceCallback<OpResult> callback);
}
