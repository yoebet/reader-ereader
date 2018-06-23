package wjy.yo.ereader.serviceimpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.DataSyncRecordDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.service.DataSyncService;

import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_BOOK_CHAPS;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_BOOK_LIST;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_CHAP_PARAS;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_PREFERENCES;
import static wjy.yo.ereader.util.Constants.DSR_DIRECTION_DOWN;

@Singleton
public class DataSyncServiceImpl implements DataSyncService {

    private DataSyncRecordDao dataSyncRecordDao;

    private Map<String, DataSyncRecord> dsrDefaults = new HashMap<>();

    @Inject
    DataSyncServiceImpl(DB db) {
        this.dataSyncRecordDao = db.dataSyncRecordDao();

        addDsrDefaults(DSR_CATEGORY_BOOK_LIST, 1, TimeUnit.HOURS);
        addDsrDefaults(DSR_CATEGORY_BOOK_CHAPS, 1, TimeUnit.DAYS);
        addDsrDefaults(DSR_CATEGORY_CHAP_PARAS, 1, TimeUnit.DAYS);
        addDsrDefaults(DSR_CATEGORY_PREFERENCES, 1, TimeUnit.DAYS);
    }

    private void addDsrDefaults(String category, int syncPeriod, TimeUnit syncPeriodUnit) {
        addDsrDefaults(category, DSR_DIRECTION_DOWN, syncPeriod, syncPeriodUnit);
    }

    private void addDsrDefaults(String category, String direction, int syncPeriod, TimeUnit syncPeriodUnit) {
        DataSyncRecord dsr = new DataSyncRecord();
        dsr.setCategory(category);
        dsr.setDirection(direction);
        dsr.setSyncPeriod(syncPeriod);
        dsr.setSyncPeriodTimeUnit(syncPeriodUnit);
        String key = category + "." + direction;
        dsrDefaults.put(key, dsr);
    }

    private DataSyncRecord getTemplateDSR(String category, String direction) {
        String key = category + "." + direction;
        DataSyncRecord dsr = dsrDefaults.get(key);
        if (dsr == null) {
            System.out.println("DSR NOT FOUND: " + key);
        } else {
            dsr = (DataSyncRecord) dsr.clone();
        }
        return dsr;
    }

    public DataSyncRecord getUserDataSyncRecord(String userName, String category, String direction) {
        DataSyncRecord dsr = dataSyncRecordDao.getUserRecord(userName, category, direction);
        if (dsr == null) {
            dsr = getTemplateDSR(category, direction);
            dsr.setUserName(userName);
            long id = dataSyncRecordDao.insert(dsr);
            dsr.setId((int) id);
            System.out.println("insert DSR: " + dsr.getId());
        }
        return dsr;
    }

    public DataSyncRecord getCommonDataSyncRecord(String category, String direction) {
        DataSyncRecord dsr = dataSyncRecordDao.getCommonRecord(category, direction);
        if (dsr == null) {
            dsr = getTemplateDSR(category, direction);
            long id = dataSyncRecordDao.insert(dsr);
            dsr.setId((int) id);
            System.out.println("insert DSR: " + dsr.getId());
        }
        return dsr;
    }

    @Override
    public void saveDataSyncRecord(DataSyncRecord dsr) {
        dataSyncRecordDao.update(dsr);
    }

    @Override
    public boolean checkTimeout(DataSyncRecord dsr) {
        return checkTimeout(dsr, dsr.getLastSyncAt());
    }

    @Override
    public boolean checkTimeout(DataSyncRecord dsr, Date lsa) {
        if (lsa == null) {
            return true;
        }

        TimeUnit spTimeUnit = dsr.getSyncPeriodTimeUnit();
        long timeout = spTimeUnit.toMillis(dsr.getSyncPeriod());

        long lastMill = lsa.getTime();
        long now = System.currentTimeMillis();

        return now - lastMill > timeout;
    }

}
