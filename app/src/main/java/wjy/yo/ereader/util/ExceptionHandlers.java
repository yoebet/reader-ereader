package wjy.yo.ereader.util;

import android.os.Looper;
import android.widget.Toast;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;

import wjy.yo.ereader.BuildConfig;
import wjy.yo.ereader.EreaderApp;

public class ExceptionHandlers {

    public static void handle(Throwable t, String defaultMessage) {
        String message = defaultMessage;

        if (t instanceof HttpException) {
            HttpException he = (HttpException) t;
            int code = he.code();
            System.out.println("HTTP " + code + ", " + he.message());
            if (code == 401) {
                message = "尚未登录";
            } else if (code == 500) {
                message = "服务器端错误";
            }
        } else if (t instanceof SocketException || t instanceof SocketTimeoutException) {
            message = "请检查网络连接";
        } else {
            System.out.println(t.getClass().getSimpleName() + ", " + t.getMessage());
            if (BuildConfig.DEBUG) {
                message = t.getMessage();
            }
        }
        if (message == null) {
            message = "发生错误了";
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(EreaderApp.context, message, Toast.LENGTH_LONG).show();
        } else {

            final String m = message;

//            Handler uiHandler = new Handler(Looper.getMainLooper());
//            uiHandler.post(() -> Toast.makeText(EreaderApp.context, m, Toast.LENGTH_LONG).show());

            AndroidSchedulers.mainThread().scheduleDirect(() ->
                    Toast.makeText(EreaderApp.context, m, Toast.LENGTH_LONG).show());
        }
    }

    public static void handle(Throwable t) {
        handle(t, null);
    }
}
