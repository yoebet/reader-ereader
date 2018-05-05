package wjy.yo.ereader.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.ChapService;
import wjy.yo.ereader.service.remote.RemoteBookService;

@Singleton
public class BookServiceImpl implements BookService {

    static final List<Book> BOOKS = new ArrayList<Book>();

    static final Map<String, Book> BOOK_MAP = new HashMap<String, Book>();
    @Inject
    RemoteBookService remoteBookService;

    static {

        Map<String, Chap> CHAP_MAP = ChapServiceImpl.CHAP_MAP;

        Book book1 = new Book("59ef3add5cb50f9b68abbc9e", "Red");
        book1.setAuthor("HKS");
        book1.setZhName("红楼梦");
        book1.setZhAuthor("cxq");
        BOOKS.add(book1);
        BOOK_MAP.put(book1.getId(), book1);
        Chap c11 = CHAP_MAP.get("11");
        Chap c12 = CHAP_MAP.get("12");
        List<Chap> b1Chaps = new ArrayList<>();
        b1Chaps.add(c11);
        b1Chaps.add(c12);
        book1.setChaps(b1Chaps);

        Book book2 = new Book("5a13e1d999a3a067a9acdecd", "Luxun");
        book2.setAuthor("yxy");
        book2.setZhName("鲁迅小说选");
        book2.setZhAuthor("LX");
        BOOKS.add(book2);
        BOOK_MAP.put(book2.getId(), book2);
        Chap c21 = CHAP_MAP.get("21");
        Chap c22 = CHAP_MAP.get("22");
        List<Chap> b2Chaps = new ArrayList<>();
        b2Chaps.add(c21);
        b2Chaps.add(c22);
        book2.setChaps(b2Chaps);
    }


    @Inject
    BookServiceImpl() {
        System.out.println("new BookServiceImpl");
    }

    public void listAllBooks(Callback<List<Book>> callback) {
//        return BOOKS;
        Call<List<Book>> call = remoteBookService.listAllBooks();
        call.enqueue(callback);
    }

//    public Book getBookLocal(String bookId) {
////        return BOOK_MAP.get(bookId);
//
//        try {
//            Call<Book> call = remoteBookService.getBook(bookId);
//            return call.execute().body();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        return null;
//    }


    public void getBook(String bookId, Callback<Book> callback) {
        Call<Book> call = remoteBookService.getBook(bookId);
        call.enqueue(callback);
    }
}
