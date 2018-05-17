package wjy.yo.ereader.di;

import android.content.Context;

import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wjy.yo.ereader.remote.AccountAPI;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.remote.LiveDataCallAdapterFactory;

@Module
public class RemoteAPI {

     final String baseUrl = "http://192.168.0.108:3000/api-b/";

    @Singleton
    @Provides
    CookieJar provideCookieJar() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return new JavaNetCookieJar(cookieManager);
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(System.out::println);
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logging;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Context context, CookieJar cookieJar, HttpLoggingInterceptor logging) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(cookieJar);
        builder.addInterceptor(logging);

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    BookAPI provideBooksAPI(Retrofit retrofit) {
        return retrofit.create(BookAPI.class);
    }

    @Singleton
    @Provides
    AccountAPI provideAccountAPI(Retrofit retrofit) {
        return retrofit.create(AccountAPI.class);
    }

}
