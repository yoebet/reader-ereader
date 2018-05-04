package wjy.yo.ereader.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;

public interface BookService {

    List<Book> listAllBooks();

    Book getBook(String bookId);

}
