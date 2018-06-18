package wjy.yo.ereader.serviceimpl;

import android.arch.persistence.room.Query;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.subjects.PublishSubject;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.DataSyncRecordDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.service.DataSyncService;

@Singleton
public class DataSyncServiceImpl implements DataSyncService {

    private DataSyncRecordDao dataSyncRecordDao;

    @Inject
    DataSyncServiceImpl(DB db) {
        System.out.println("new DataSyncServiceImpl");

        this.dataSyncRecordDao = db.dataSyncRecordDao();
    }

    public DataSyncRecord getUserDataSyncRecord(String userName, String category, String direction) {
        return null;
    }

    public DataSyncRecord getCommonDataSyncRecord(String category, String direction) {

        return null;
    }

}
