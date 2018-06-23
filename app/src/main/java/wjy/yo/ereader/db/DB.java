package wjy.yo.ereader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import wjy.yo.ereader.db.anno.AnnoFamilyDao;
import wjy.yo.ereader.db.book.BookDao;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.db.book.ParaDao;
import wjy.yo.ereader.db.dict.MeaningItemDao;
import wjy.yo.ereader.db.dict.WordRankDao;
import wjy.yo.ereader.db.dict.DictDao;
import wjy.yo.ereader.db.dict.WordCategoryDao;
import wjy.yo.ereader.db.userdata.UserBookDao;
import wjy.yo.ereader.db.userdata.UserChapDao;
import wjy.yo.ereader.db.userdata.UserDao;
import wjy.yo.ereader.db.userdata.PreferenceDao;
import wjy.yo.ereader.db.userdata.UserWordDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.LocalSetting;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entity.dict.Dict;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.Preference;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.entity.userdata.UserBook;
import wjy.yo.ereader.entity.userdata.UserChap;
import wjy.yo.ereader.entity.userdata.UserWord;

@Database(entities = {
        AnnoFamily.class,
        Book.class,
        Chap.class,
        Para.class,
        WordRank.class,
        Dict.class,
        MeaningItem.class,
        WordCategory.class,
        User.class,
        UserBook.class,
        UserChap.class,
        UserWord.class,
        Preference.class,
        DataSyncRecord.class,
        LocalSetting.class
}, version = 1)
@TypeConverters(DateTimeConverter.class)
public abstract class DB extends RoomDatabase {

    abstract public AnnoFamilyDao annoFamilyDao();


    abstract public BookDao bookDao();

    abstract public ChapDao chapDao();

    abstract public ParaDao paraDao();


    abstract public DictDao dictDao();

    abstract public WordRankDao wordRankDao();

    abstract public MeaningItemDao meaningItemDao();

    abstract public WordCategoryDao wordCategoryDao();


    abstract public UserDao userDao();

    abstract public UserBookDao userBookDao();

    abstract public UserChapDao userChapDao();

    abstract public UserWordDao userWordDao();

    abstract public PreferenceDao preferenceDao();

    abstract public DataSyncRecordDao dataSyncRecordDao();


    abstract public LocalSettingDao localSettingDao();
}
