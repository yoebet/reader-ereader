package wjy.yo.ereader.remote;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.userdata.UserBook;

public interface UserBookAPI {

    @GET("user_books/withChaps")
    Single<List<UserBook>> getMyBooks();

//    @GET("user_books/{bookId}")
//    Single<List<UserBook>> getMyBookChaps(@Path("bookId") String bookId);
}
