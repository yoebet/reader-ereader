package wjy.yo.ereader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import wjy.yo.ereader.model.Book;

@Database(entities = Book.class, version = 1)
public abstract class DB extends RoomDatabase {

    abstract public BookDao bookDao();
}
