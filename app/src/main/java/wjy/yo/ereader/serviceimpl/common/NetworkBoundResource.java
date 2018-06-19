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

    private M lastValue = null;

    private final Flowable<M> result;

    @MainThread
    public NetworkBoundResource() {

        result = Flowable.create(emitter -> {
            System.out.println("t1 " + Thread.currentThread());

            Flowable<M> dbSource = loadFromDb();
            Disposable disposable = dbSource.subscribe((M data) -> {
                System.out.println("t2 " + Thread.currentThread());
                System.out.println(".. emitter, from DB");
                setValue(data, emitter);
                if (shouldFetch(data)) {
                    fetchFromNetwork(data);
                }
            }, this::onError);
        }, BackpressureStrategy.LATEST);

    }


    private void fetchFromNetwork(M postedData) {

        Flowable<M> flowable = createCall();

        Disposable disposable = flowable.subscribe(newData -> {
            System.out.println("t3 " + Thread.currentThread());
            if (newData == null) {
                return;
            }
//            if (Objects.equals(newData, postedData)) {
//                System.out.println("Received From Network ... Unchanged.");
//                return;
//            }

            System.out.println("Received From Network ...");

            saveCallResult(newData, postedData);
        }, this::onError);
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


    protected abstract Flowable<M> loadFromDb();

    protected abstract boolean shouldFetch(@Nullable M data);

    @NonNull
    protected abstract Flowable<M> createCall();

    @WorkerThread
    protected abstract void saveCallResult(M data, M oldData);

    protected void onError(Throwable t) {
        if (t != null) {
            t.printStackTrace();
        }
    }

}
