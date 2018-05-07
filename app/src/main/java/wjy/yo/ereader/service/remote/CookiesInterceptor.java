package wjy.yo.ereader.service.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CookiesInterceptor implements Interceptor {
    public static final String PREF_COOKIES = "PREF_COOKIES";
    private Context context;

    public CookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        HashSet<String> preferences = (HashSet<String>) pref.getStringSet(PREF_COOKIES, new HashSet<String>());

        for (String cookie : preferences) {
            System.out.println("add cookie: " + cookie);
            builder.addHeader("Cookie", cookie);
        }

        Response response = chain.proceed(builder.build());

        if (!response.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = (HashSet<String>) pref.getStringSet(PREF_COOKIES, new HashSet<String>());

            List<String> headers = response.headers("Set-Cookie");
            System.out.println("set cookie: " + headers);
            cookies.addAll(headers);

            SharedPreferences.Editor memes = pref.edit();
            memes.putStringSet(PREF_COOKIES, cookies).apply();
            memes.commit();
        }

        return response;
    }
}
