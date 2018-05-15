package wjy.yo.ereader.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wjy.yo.ereader.db.BookDao;
import wjy.yo.ereader.db.ChapDao;
import wjy.yo.ereader.db.DB;

@Module
class DbModule {
    @Singleton
    @Provides
    DB provideDb(Application app) {
        return Room.databaseBuilder(app, DB.class, "ereader.db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
//                .fallbackToDestructiveMigration()
                .fallbackToDestructiveMigrationFrom(1)
                .build();
    }

    @Singleton
    @Provides
    BookDao provideBookDao(DB db) {
        return db.bookDao();
    }

    @Singleton
    @Provides
    ChapDao provideChapDao(DB db) {
        return db.chapDao();
    }
}
