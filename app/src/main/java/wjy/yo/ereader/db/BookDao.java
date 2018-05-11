package wjy.yo.ereader.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.model.Book;

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book... books);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooks(List<Book> books);

    @Query("SELECT * FROM book")
    LiveData<List<Book>> loadBooks();

    @Query("SELECT * FROM book WHERE _id = :id")
    LiveData<Book> loadBook(String id);
}
