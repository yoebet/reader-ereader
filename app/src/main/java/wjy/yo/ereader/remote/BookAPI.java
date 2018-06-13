package wjy.yo.ereader.remote;

import android.arch.lifecycle.LiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.entityvo.book.ChapDetail;

public interface BookAPI {

    @GET("books/")
    LiveData<List<Book>> listAllBooks();

    @GET("books/{bookId}/detail")
    LiveData<BookDetail> getBookDetail(@Path("bookId") String bookId);

    @GET("chaps/{chapId}/detail")
    LiveData<ChapDetail> getChapDetail(@Path("chapId") String chapId);
}
