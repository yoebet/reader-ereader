package wjy.yo.ereader.service.impl;

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
import wjy.yo.ereader.remote.BookAPI;

@Singleton
public class BookServiceImpl implements BookService {

//    @Inject
//    BookAPI bookAPI;

    @Inject
    BookServiceImpl() {
        System.out.println("new BookServiceImpl");
    }
}
