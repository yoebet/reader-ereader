package wjy.yo.ereader.remote;

import android.arch.lifecycle.LiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.model.Book;

public interface BooksAPI {

    @GET("books/")
    Call<List<Book>> listAllBooks();

    @GET("books/")
    LiveData<List<Book>> listAllBooks2();

    @GET("books/{bookId}/detail")
    Call<Book> getBook(@Path("bookId") String bookId);

    @GET("books/{bookId}/detail")
    LiveData<Book> getBookWithChaps(@Path("bookId") String bookId);
}
