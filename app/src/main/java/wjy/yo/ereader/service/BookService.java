package wjy.yo.ereader.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjy.yo.ereader.model.Book;

public class BookService {

    public static final List<Book> BOOKS = new ArrayList<Book>();

    public static final Map<String, Book> BOOK_MAP = new HashMap<String, Book>();


    static {
        Book book1 = new Book("123", "Red");
        book1.setAuthor("HKS");
        book1.setZhName("红楼梦");
        book1.setZhAuthor("cxq");
        BOOKS.add(book1);
        BOOK_MAP.put(book1.getId(), book1);
        Book book2 = new Book("234", "Luxun");
        book2.setAuthor("yxy");
        book2.setZhName("鲁迅小说选");
        book2.setZhAuthor("LX");
        BOOKS.add(book2);
        BOOK_MAP.put(book2.getId(), book2);
    }


}
