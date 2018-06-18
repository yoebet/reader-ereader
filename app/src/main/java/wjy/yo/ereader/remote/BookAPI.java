package wjy.yo.ereader.remote;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.IdVersion;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.entityvo.book.ChapDetail;

public interface BookAPI {

    @GET("books/")
    Flowable<List<Book>> listAllBooks();

    @GET("books/{bookId}/detail")
    Flowable<BookDetail> getBookDetail(@Path("bookId") String bookId);

    @GET("books/{bookId}/chapVersions")
    Flowable<List<IdVersion>> getChapVersions(@Path("bookId") String bookId);

    @GET("chaps/{chapId}")
    Flowable<Chap> getChap(@Path("chapId") String chapId);

    @GET("chaps/{chapId}/detail")
    Flowable<ChapDetail> getChapDetail(@Path("chapId") String chapId);

    @GET("chaps/{chapId}/paraVersions")
    Flowable<List<IdVersion>> getParaVersions(@Path("chapId") String chapId);
}
