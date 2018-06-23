package wjy.yo.ereader.serviceimpl.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


public abstract class NetworkBoundResource<M> {

    private M lastValue = null;

    private final Flowable<M> result;

    private String label;

    protected NetworkBoundResource(String label) {
        this.label = label;

        result = Flowable.create(emitter -> {

            Flowable<M> dbSource = loadFromDb();
            Disposable disposable = dbSource.subscribe((M data) -> {
                System.out.println(label + "  .. emitter, from DB");
                setValue(data, emitter);
                if (shouldFetch(data)) {
                    fetchFromNetwork(data);
                }
            }, this::onError);
            emitter.setDisposable(disposable);
        }, BackpressureStrategy.LATEST);

    }


    private void fetchFromNetwork(M postedData) {

        Disposable disposable = createCall()
                .subscribe(newData -> {
                    if (newData == null) {
                        return;
                    }

                    System.out.println(label + "  Received From Network ...");
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
    protected abstract Observable<M> createCall();

    protected abstract void saveCallResult(M data, M oldData);

    protected void onError(Throwable t) {
        if (t != null) {
            t.printStackTrace();
        }
    }

}
