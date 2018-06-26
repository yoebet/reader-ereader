package wjy.yo.ereader.remote;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.userdata.UserBook;

public interface UserBookAPI {

    @GET("user_books/")
    Observable<List<UserBook>> getMyBooks();

    @GET("user_books/withChaps")
    Observable<List<UserBook>> getMyBookWithChaps();

    @GET("user_books/{bookId}")
    Observable<List<UserBook>> getMyBookChaps(@Path("bookId") String bookId);
}
