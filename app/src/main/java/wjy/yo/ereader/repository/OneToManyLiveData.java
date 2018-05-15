package wjy.yo.ereader.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import java.util.List;

import wjy.yo.ereader.util.BiFunction;

class OneToManyLiveData<O, M> extends MutableLiveData<O> {
    private O oModel;
    private List<M> mList;

    private LiveData<O> liveDataO;
    private LiveData<List<M>> liveDataMs;
    private BiFunction<O, List<M>> setter;
    private Observer<O> observer1;
    private Observer<List<M>> observer2;

    OneToManyLiveData(LiveData<O> liveDataO, LiveData<List<M>> liveDataMs, BiFunction<O, List<M>> setter) {
        this.liveDataO = liveDataO;
        this.liveDataMs = liveDataMs;
        this.setter = setter;
        observer1 = o1 -> {
            oModel = o1;
            checkComplete();
        };
        liveDataO.observeForever(observer1);
        observer2 = ms1 -> {
            mList = ms1;
            checkComplete();
        };
        liveDataMs.observeForever(observer2);
    }


    private synchronized void checkComplete() {
        if (oModel != null && mList != null) {
            this.setter.apply(oModel, mList);
            setValue(oModel);
            liveDataO.removeObserver(observer1);
            liveDataMs.removeObserver(observer2);
            liveDataO = null;
            liveDataMs = null;
        }
    }

}
