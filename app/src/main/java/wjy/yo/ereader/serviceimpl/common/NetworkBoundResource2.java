package wjy.yo.ereader.serviceimpl.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;


public abstract class NetworkBoundResource2<M> {

    private M lastValue = null;

    private final Flowable<M> result;

    private String label;

    protected NetworkBoundResource2(String label) {
        this.label = label;

        result = Flowable.create(emitter -> {

            Maybe<M> dbSource = loadFromDb();
            MaybeObserver<M> mo = new MaybeObserver<M>() {

                @Override
                public void onSubscribe(Disposable d) {
//                    System.out.println(label + " .. onSubscribe");
                }

                @Override
                public void onSuccess(M m) {
                    System.out.println(label + " .. onSuccess");
                    if (shouldFetch(m)) {
                        fetchFromNetwork(m, emitter);
                    } else {
                        setValue(m, emitter);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println(label + " .. onError");
                }

                @Override
                public void onComplete() {
                    System.out.println(label + " .. onComplete");
                    fetchFromNetwork(null, emitter);
                }
            };
            dbSource.subscribe(mo);

        }, BackpressureStrategy.LATEST);

    }


    private void fetchFromNetwork(M postedData, FlowableEmitter<M> emitter) {

        Disposable disposable = createCall()
                .subscribe(newData -> {
                    System.out.println(label + "  Received From Network ...");
                    saveCallResult(newData, postedData);
                    setValue(newData, emitter);
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


    protected abstract Maybe<M> loadFromDb();

    protected abstract boolean shouldFetch(@Nullable M data);

    @NonNull
    protected abstract Maybe<M> createCall();

    protected abstract void saveCallResult(M data, M oldData);

    protected void onError(Throwable t) {
        if (t != null) {
            t.printStackTrace();
        }
    }

}
