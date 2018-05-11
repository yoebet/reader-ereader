package wjy.yo.ereader.repository;

import android.arch.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        Class<?> rawType = getRawType(returnType);
        if (/*rawType != Call.class && */rawType != LiveData.class) {
            return null;
        }

        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as LiveData<Foo> or LiveData<? extends Foo>");
        }

        Type bodyType = getParameterUpperBound(0, (ParameterizedType) returnType);

/*        if (rawType == Call.class) {
            return new CallAdapter<Object, Call<?>>() {
                @Override
                public Type responseType() {
                    return bodyType;
                }

                @Override
                public Call<Object> adapt(Call<Object> call) {
                    return call;
                }
            };
        }*/

        return new LiveDataCallAdapter<>(bodyType);
    }
}
