package wjy.yo.ereader.service.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.service.BookService;

@Singleton
public class BookServiceImpl implements BookService {

//    @Inject
//    BookAPI bookAPI;

    @Inject
    BookServiceImpl() {
        System.out.println("new BookServiceImpl");
    }
}
