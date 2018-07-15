package wjy.yo.ereader.util;

import retrofit2.HttpException;

public class ExceptionHandlers {

    public static void handle(Throwable t) {
        if (t instanceof HttpException) {
            HttpException he = (HttpException) t;
            System.out.println("HTTP " + he.code() + ", " + he.message());
        } else {
            System.out.println(t.getClass().getSimpleName() + ", " + t.getMessage());
        }
    }
}
