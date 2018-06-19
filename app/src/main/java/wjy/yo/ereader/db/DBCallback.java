package wjy.yo.ereader.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

public class DBCallback extends RoomDatabase.Callback {


    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);

        db.execSQL("",new Object[]{});
    }
}
