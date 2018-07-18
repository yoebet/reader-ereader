package wjy.yo.ereader.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import static wjy.yo.ereader.util.Constants.FTS_TABLE_PARA_CONTENT;

public class DBCallback extends RoomDatabase.Callback {


    public void onCreate(@NonNull SupportSQLiteDatabase db) {

        final String T = FTS_TABLE_PARA_CONTENT;

        db.execSQL("CREATE VIRTUAL TABLE " + T + " USING fts4(content, paraId, chapId, bookId," +
                " tokenize=porter, notindexed=paraId, notindexed=chapId, notindexed=bookId)");

        db.execSQL("CREATE TRIGGER after_para_insert AFTER INSERT ON book_para" +
                " BEGIN" +
                "  INSERT INTO " + T + " (content, paraId, chapId, bookId)" +
                "  VALUES( new.content, new.id, new.chapId, new.bookId);" +
                "END;");

        db.execSQL("CREATE TRIGGER after_para_update AFTER UPDATE ON book_para" +
                " BEGIN" +
                "  UPDATE " + T + " SET content = new.content WHERE paraId = old.id;" +
                "END;");

        db.execSQL("CREATE TRIGGER after_para_delete AFTER DELETE ON book_para BEGIN" +
                "  DELETE FROM " + T + " WHERE paraId = old.id;" +
                "END;");

    }
}
