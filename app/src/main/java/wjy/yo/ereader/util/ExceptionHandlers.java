package wjy.yo.ereader.util;

import android.widget.Toast;

import retrofit2.HttpException;
import wjy.yo.ereader.EreaderApp;

public class ExceptionHandlers {

    public static void handle(Throwable t) {
        if (t instanceof HttpException) {
            HttpException he = (HttpException) t;
            System.out.println("HTTP " + he.code() + ", " + he.message());
        } else {
            System.out.println(t.getClass().getSimpleName() + ", " + t.getMessage());
            Toast.makeText(EreaderApp.context, t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
