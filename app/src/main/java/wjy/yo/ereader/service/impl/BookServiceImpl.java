package wjy.yo.ereader.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.ChapService;

@Singleton
public class BookServiceImpl implements BookService {

    static final List<Book> BOOKS = new ArrayList<Book>();

    static final Map<String, Book> BOOK_MAP = new HashMap<String, Book>();


    static {

        Map<String, Chap> CHAP_MAP = ChapServiceImpl.CHAP_MAP;

        Book book1 = new Book("123", "Red");
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

        Book book2 = new Book("234", "Luxun");
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
    public BookServiceImpl(){
        System.out.println("new BookServiceImpl");
    }

    public List<Book> listAllBooks() {
        return BOOKS;
    }

    public Book getBook(String bookId){
        return BOOK_MAP.get(bookId);
    }
}
