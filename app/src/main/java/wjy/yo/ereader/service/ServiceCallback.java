package wjy.yo.ereader.service;

import wjy.yo.ereader.vo.Failure;

public interface ServiceCallback<M> {
    void onComplete(M m);
    void onFailure(Failure f);
}
