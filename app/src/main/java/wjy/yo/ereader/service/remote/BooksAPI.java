package wjy.yo.ereader.service.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.model.Book;

public interface BooksAPI {

    @GET("books/")
    Call<List<Book>> listAllBooks();

    @GET("books/{bookId}/detail")
    Call<Book> getBook(@Path("bookId") String bookId);
}
