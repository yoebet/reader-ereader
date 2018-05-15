package wjy.yo.ereader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;

@Database(entities = {Book.class, Chap.class}, version = 2)
public abstract class DB extends RoomDatabase {

    abstract public BookDao bookDao();

    abstract public ChapDao chapDao();
}
