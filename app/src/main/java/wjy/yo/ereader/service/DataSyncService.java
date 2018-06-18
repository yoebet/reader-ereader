package wjy.yo.ereader.service;

import wjy.yo.ereader.entity.DataSyncRecord;

public interface DataSyncService {

    DataSyncRecord getUserDataSyncRecord(String userName, String category, String direction);

    DataSyncRecord getCommonDataSyncRecord(String category, String direction);
}
