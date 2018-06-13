package wjy.yo.ereader.serviceimpl.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.Objects;

import wjy.yo.ereader.util.AppExecutors;


public abstract class NetworkBoundResource<M> {
    private final AppExecutors appExecutors;

    //TODO: from configuration
    private boolean offline = false;

    private LiveData<M> dbSource;

    private final MediatorLiveData<M> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        dbSource = loadFromDb();
        result.addSource(dbSource, this::onDbData);
    }

    private void onDbData(M data) {
        System.out.println(".. setValue, from DB");
        setValue(data);
        if (!offline && shouldFetch(data)) {
            fetchFromNetwork(data);
        }
    }

    private void addDbSource() {
        appExecutors.mainThread().execute(() -> {
            dbSource = loadFromDb();
            result.addSource(dbSource, this::onDbData);
        });
    }

    private void fetchFromNetwork(M postedData) {
        result.removeSource(this.dbSource);

        LiveData<M> liveData = createCall();
        result.addSource(liveData, newData -> {
            result.removeSource(liveData);
            if (newData == null) {
                //TODO:
                addDbSource();
                return;
            }
            if (Objects.equals(newData, postedData)) {
                System.out.println("Received From Network ... Unchanged.");
                addDbSource();
                return;
            }

//            System.out.println(".. setValue, from Network");
//            setValue(newData);
            System.out.println("Received From Network ... Replaced.");
            appExecutors.diskIO().execute(() -> {
                System.out.println("t3 " + Thread.currentThread());
                System.out.println(".. saveCallResult");
                saveCallResult(newData, postedData);
                addDbSource();
            });
        });
    }

    private void setValue(M data) {
        setOrPostValue(data, true);
    }

    private void setOrPostValue(M data, boolean set) {
        M ov = result.getValue();
        if (Objects.equals(data, ov)) {
//            System.out.println(".. try postValue, Equals.");
            return;
        }
        if (set) {
            result.setValue(data);
        } else {
            result.postValue(data);
        }
    }

    public LiveData<M> asLiveData() {
        return result;
    }


    @WorkerThread
    protected abstract void saveCallResult(M data, M oldData);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable M data);

    @MainThread
    protected abstract LiveData<M> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<M> createCall();
}
