package wjy.yo.ereader.service;

import java.util.Date;

import wjy.yo.ereader.entity.DataSyncRecord;

public interface DataSyncService {

    DataSyncRecord getUserDataSyncRecord(String userName, String category, String direction);

    DataSyncRecord getCommonDataSyncRecord(String category, String direction);

    void saveDataSyncRecord(DataSyncRecord dsr);

    boolean checkTimeout(DataSyncRecord dsr);

    boolean checkTimeout(DataSyncRecord dsr, Date lastSyncAt);
}
