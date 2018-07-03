package wjy.yo.ereader.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;

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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import wjy.yo.ereader.BuildConfig;
import wjy.yo.ereader.remote.AnnotationsAPI;
import wjy.yo.ereader.remote.DictAPI;
import wjy.yo.ereader.remote.AccountAPI;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.remote.PreferenceAPI;
import wjy.yo.ereader.remote.UserBookAPI;
import wjy.yo.ereader.remote.UserWordAPI;

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

        if (BuildConfig.DEBUG) {
            ChuckInterceptor chuck = new ChuckInterceptor(context);
            // chuck.showNotification(false);
            builder.addInterceptor(chuck);
        }

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    AccountAPI provideAccountAPI(Retrofit retrofit) {
        return retrofit.create(AccountAPI.class);
    }

    @Singleton
    @Provides
    AnnotationsAPI provideAnnotationsAPI(Retrofit retrofit) {
        return retrofit.create(AnnotationsAPI.class);
    }

    @Singleton
    @Provides
    BookAPI provideBooksAPI(Retrofit retrofit) {
        return retrofit.create(BookAPI.class);
    }

    @Singleton
    @Provides
    UserBookAPI provideUserBooksAPI(Retrofit retrofit) {
        return retrofit.create(UserBookAPI.class);
    }

    @Singleton
    @Provides
    DictAPI provideDictAPI(Retrofit retrofit) {
        return retrofit.create(DictAPI.class);
    }

    @Singleton
    @Provides
    PreferenceAPI providePreferenceAPI(Retrofit retrofit) {
        return retrofit.create(PreferenceAPI.class);
    }

    @Singleton
    @Provides
    UserWordAPI provideUserWordAPI(Retrofit retrofit) {
        return retrofit.create(UserWordAPI.class);
    }

}
