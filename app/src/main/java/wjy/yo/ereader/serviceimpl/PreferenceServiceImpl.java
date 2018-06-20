package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.PreferenceDao;
import wjy.yo.ereader.db.userdata.UserWordTagDao;
import wjy.yo.ereader.entity.userdata.Preference;
import wjy.yo.ereader.entity.userdata.UserWordTag;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.PreferenceService;

import static wjy.yo.ereader.util.Constants.PREF_BASE_VOCABULARY;

@Singleton
public class PreferenceServiceImpl extends UserDataService implements PreferenceService {

    private PreferenceDao preferenceDao;

    private UserWordTagDao userWordTagDao;

    private Map<String, Preference> preferencesMap;

    private List<UserWordTag> userWordTags;

    @Inject
    @SuppressLint("CheckResult")
    public PreferenceServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.preferenceDao = db.preferenceDao();
        this.userWordTagDao = db.userWordTagDao();
    }

    protected void onUserChanged() {
        userWordTags = null;
        preferencesMap = null;
        loadUserPreferences();
    }

    @SuppressLint("CheckResult")
    private void loadUserPreferences() {
        if (userName == null) {
            return;
        }
        preferenceDao.loadUserPreferences(userName)
                .subscribe((List<Preference> pl) -> {
                    if (preferencesMap == null) {
                        preferencesMap = new HashMap<>();
                    } else {
                        preferencesMap.clear();
                    }
                    for (Preference p : pl) {
                        preferencesMap.put(p.getCode(), p);
                    }
                });
    }


    private String getPreference(String code) {
        if (preferencesMap == null) {
            return null;
        }
        Preference p = preferencesMap.get(code);
        if (p == null) {
            return null;
        }
        return p.getValue();
    }

    private void setPreferenceAsync(String code, String value) {
        if (preferencesMap == null) {
            return;
        }
        Schedulers.io().scheduleDirect(() -> {
            Preference pref = preferencesMap.get(code);
            if (pref != null) {
                pref.setValue(value);
                updateUserData(pref);
                preferenceDao.update(pref);

                //TODO: sync
                return;
            }
            pref = new Preference();
            setupNewUserData(pref);
            pref.setCode(code);
            pref.setValue(value);
            preferencesMap.put(code, pref);
            preferenceDao.insert(pref);

            //TODO:
        });
    }

    public String getBaseVocabulary() {
        return getPreference(PREF_BASE_VOCABULARY);
    }


    public void setBaseVocabulary(String categoryCode) {
        setPreferenceAsync(PREF_BASE_VOCABULARY, categoryCode);
    }


    public Flowable<List<UserWordTag>> getUserWordTags() {
        if (userWordTags != null) {
            return Flowable.just(userWordTags);
        }
        return userWordTagDao.loadUserWordTags(userName)
                .map((List<UserWordTag> ts) -> {
                    this.userWordTags = ts;
                    return ts;
                });
    }

}
