package wjy.yo.ereader.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import wjy.yo.ereader.util.AppExecutors;


public abstract class NetworkBoundResource<M> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<M> result = new MediatorLiveData<>();

    @MainThread
    NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        LiveData<M> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            if (shouldFetch(data)) {
                System.out.println("fetchFromNetwork ...");
                fetchFromNetwork(dbSource);
            } else {
                result.postValue(data);
            }
        });
    }

//    @MainThread
//    private void setValue(M newValue) {
//        M value = result.getValue();
//        if (newValue != null && !newValue.equals(value)) {
//            result.setValue(newValue);
//        }
//    }

    private void fetchFromNetwork(final LiveData<M> dbSource) {
        LiveData<M> liveData = createCall();
        result.addSource(liveData, data -> {
            result.removeSource(liveData);
            System.out.println("Received From Network ...");
            if (data == null) {
                result.postValue(dbSource.getValue());
                //TODO:
                return;
            }
            result.postValue(data);
            appExecutors.diskIO().execute(() -> {
                saveCallResult(data);
/*                appExecutors.mainThread().execute(() -> {
                            result.removeSource(dbSource);
                            result.addSource(loadFromDb(),
                                    result::setValue);
                        }
                );*/
            });
        });
    }

    protected void onFetchFailed() {
    }

    public LiveData<M> asLiveData() {
        return result;
    }


    @WorkerThread
    abstract void saveCallResult(M data);

    @MainThread
    abstract boolean shouldFetch(@Nullable M data);

    @MainThread
    abstract LiveData<M> loadFromDb();

    @NonNull
    @MainThread
    abstract LiveData<M> createCall();
}
