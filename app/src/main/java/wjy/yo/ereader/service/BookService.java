package wjy.yo.ereader.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chapter;

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
        Chapter c11 = new Chapter("11", "Chapter 1 fs", "第一章 zz");
        Chapter c12 = new Chapter("12", "Chapter 2 zdg", "第二章 ee");
        List<Chapter> b1Chapters = new ArrayList<>();
        b1Chapters.add(c11);
        b1Chapters.add(c12);
        book1.setChapters(b1Chapters);

        Book book2 = new Book("234", "Luxun");
        book2.setAuthor("yxy");
        book2.setZhName("鲁迅小说选");
        book2.setZhAuthor("LX");
        BOOKS.add(book2);
        BOOK_MAP.put(book2.getId(), book2);
        Chapter c21 = new Chapter("21", "Chapter 1 TSDs", "第一章 方法");
        Chapter c22 = new Chapter("22", "Chapter 2 GSWEW", "第二章 说说");
        List<Chapter> b2Chapters = new ArrayList<>();
        b2Chapters.add(c21);
        b2Chapters.add(c22);
        book2.setChapters(b2Chapters);
    }


}
