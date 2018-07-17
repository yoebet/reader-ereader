package wjy.yo.ereader.remote;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.entityvo.book.ChapDetail;

public interface BookAPI {

    @GET("books/")
    Single<List<Book>> listAllBooks();

    @GET("books/{bookId}/detail")
    Maybe<BookDetail> getBookDetail(@Path("bookId") String bookId);

    @GET("books/{bookId}/chapVersions")
    Single<List<FetchedData>> getChapVersions(@Path("bookId") String bookId);

    @GET("chaps/{chapId}")
    Maybe<Chap> getChap(@Path("chapId") String chapId);

    @GET("chaps/{chapId}/detail")
    Maybe<ChapDetail> getChapDetail(@Path("chapId") String chapId);

    @GET("chaps/{chapId}/paraVersions")
    Single<List<FetchedData>> getParaVersions(@Path("chapId") String chapId);

    @GET("paras/search/{word}")
    Single<List<Para>> textSearch(@Path("word") String word, @Query("limit") int limit);
}
