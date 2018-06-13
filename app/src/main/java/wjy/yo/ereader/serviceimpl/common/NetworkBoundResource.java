package wjy.yo.ereader.serviceimpl.common;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.Disposable;


public abstract class NetworkBoundResource<M> {

    //TODO: from configuration
    private boolean offline = false;

    private M lastValue = null;

    private final Flowable<M> result;

    @MainThread
    public NetworkBoundResource() {

        result = Flowable.create(emitter -> {
            System.out.println("t1 " + Thread.currentThread());

            Flowable<M> dbSource = loadFromDb();
            Disposable disposable = dbSource.subscribe((M data) -> {
                if (emitter != null) {
                    System.out.println(".. emitter, from DB");
                    setValue(data, emitter);
                    if (!offline && shouldFetch(data)) {
                        fetchFromNetwork(data);
                    }
                }
            });
        }, BackpressureStrategy.LATEST);

    }


    private void fetchFromNetwork(M postedData) {

        Flowable<M> flowable = createCall();

        Disposable disposable = flowable.subscribe(newData -> {
            System.out.println("t2 " + Thread.currentThread());
            if (newData == null) {
                return;
            }
            if (Objects.equals(newData, postedData)) {
                System.out.println("Received From Network ... Unchanged.");
                return;
            }

            System.out.println("Received From Network ... Replaced.");

            System.out.println(".. saveCallResult");
            saveCallResult(newData, postedData);
        });
    }

    private void setValue(M data, FlowableEmitter<M> emitter) {
        if (Objects.equals(data, lastValue)) {
            return;
        }
        lastValue = data;
        if (data != null && emitter != null) {
            emitter.onNext(data);
        }
    }

    public Flowable<M> asFlowable() {
        return result;
    }


    @WorkerThread
    protected abstract void saveCallResult(M data, M oldData);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable M data);

    @MainThread
    protected abstract Flowable<M> loadFromDb();

    @NonNull
    @MainThread
    protected abstract Flowable<M> createCall();
}
