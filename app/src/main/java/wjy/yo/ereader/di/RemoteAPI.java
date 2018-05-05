package wjy.yo.ereader.di;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wjy.yo.ereader.service.remote.RemoteBookService;

@Module
public class RemoteAPI {

    final String baseUrl = "http://192.168.0.103:3000/api-b/";

    @Singleton
    @Provides
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

    @Singleton
    @Provides
    @Inject
    RemoteBookService provideRemoteBookService(Retrofit retrofit) {
        return retrofit.create(RemoteBookService.class);
    }

}
