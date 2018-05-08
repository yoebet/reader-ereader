package wjy.yo.ereader.di;

import android.content.Context;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wjy.yo.ereader.EreaderApp;
import wjy.yo.ereader.service.remote.AccountAPI;
import wjy.yo.ereader.service.remote.BooksAPI;
import wjy.yo.ereader.service.remote.CookiesInterceptor;

@Module
public class RemoteAPI {

    final String baseUrl = "http://192.168.0.103:3000/api-b/";


    @Singleton
    @Provides
    CookieJar provideCookieJar() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return new JavaNetCookieJar(cookieManager);
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Context context, CookieJar cookieJar) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(cookieJar);
//        builder.addInterceptor(new CookiesInterceptor(context));

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        builder.addInterceptor(logging);

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    BooksAPI provideBooksAPI(Retrofit retrofit) {
        return retrofit.create(BooksAPI.class);
    }

    @Singleton
    @Provides
    AccountAPI provideAccountAPI(Retrofit retrofit) {
        return retrofit.create(AccountAPI.class);
    }

}
