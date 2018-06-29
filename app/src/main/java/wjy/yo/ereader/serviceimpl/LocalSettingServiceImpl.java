package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.LocalSettingDao;
import wjy.yo.ereader.entity.LocalSetting;
import wjy.yo.ereader.service.LocalSettingService;

import static wjy.yo.ereader.util.Constants.SETTING_OFFLINE;

@Singleton
public class LocalSettingServiceImpl implements LocalSettingService {

    private LocalSettingDao localSettingDao;

    private Map<String, LocalSetting> settingsMap;

    @Inject
    public LocalSettingServiceImpl(DB db) {
        this.localSettingDao = db.localSettingDao();
        Schedulers.io().scheduleDirect(this::loadAll);
    }


    @SuppressLint("CheckResult")
    private void loadAll() {
        localSettingDao.loadAll()
                .subscribe((List<LocalSetting> sl) -> {
                    if (settingsMap == null) {
                        settingsMap = new HashMap<>();
                    } else {
                        settingsMap.clear();
                    }
                    for (LocalSetting s : sl) {
                        settingsMap.put(s.getCode(), s);
                    }
                });
    }


    private String getSetting(String code) {
        if (settingsMap == null) {
            return null;
        }
        LocalSetting s = settingsMap.get(code);
        if (s == null) {
            return null;
        }
        return s.getValue();
    }

    private void setSettingAsync(String code, String value) {
        if (settingsMap == null) {
            return;
        }
        Schedulers.io().scheduleDirect(() -> {
            LocalSetting setting = settingsMap.get(code);
            if (setting != null) {
                setting.setValue(value);
                setting.setUpdatedAt(new Date());
                localSettingDao.update(setting);
                return;
            }
            setting = new LocalSetting(code);
            setting.setValue(value);
            setting.setUpdatedAt(new Date());
            settingsMap.put(code, setting);
            localSettingDao.insert(setting);
        });
    }

    public boolean isOffline() {
        String sv = getSetting(SETTING_OFFLINE);
        return "Y".equals(sv);
    }

    public void setOffline(boolean offline) {
        String sv = offline ? "Y" : "N";
        setSettingAsync(SETTING_OFFLINE, sv);
    }
}
