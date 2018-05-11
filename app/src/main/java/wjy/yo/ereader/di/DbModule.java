package wjy.yo.ereader.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wjy.yo.ereader.db.BookDao;
import wjy.yo.ereader.db.DB;

@Module
class DbModule {
    @Singleton
    @Provides
    DB provideDb(Application app) {
        return Room.databaseBuilder(app, DB.class,"ereader.db").build();
    }

    @Singleton @Provides
    BookDao provideUserDao(DB db) {
        return db.bookDao();
    }
}
