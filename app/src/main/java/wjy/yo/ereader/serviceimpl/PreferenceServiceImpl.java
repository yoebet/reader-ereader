package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.PreferenceDao;
import wjy.yo.ereader.db.userdata.UserWordTagDao;
import wjy.yo.ereader.entity.userdata.Preference;
import wjy.yo.ereader.entity.userdata.UserWordTag;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.PreferenceService;

import static wjy.yo.ereader.util.Constants.PC_BASE_VOCABULARY;

@Singleton
public class PreferenceServiceImpl extends UserDataService implements PreferenceService {

    private PreferenceDao preferenceDao;

    private UserWordTagDao userWordTagDao;

    private Map<String, String> preferenceMap;

    private List<UserWordTag> userWordTags;

    //TODO:
    private List<Preference> unsavedPreference;

    @Inject
    @SuppressLint("CheckResult")
    public PreferenceServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService,dataSyncService);
        this.preferenceDao = db.preferenceDao();
        this.userWordTagDao = db.userWordTagDao();

        this.unsavedPreference = new ArrayList<>();
    }

    protected void onUserChanged() {
        userWordTags = null;
        this.preferenceMap = null;
    }

    private Flowable<Map<String, String>> getPreferenceMap() {
        if (preferenceMap != null) {
            return Flowable.just(preferenceMap);
        }
        return preferenceDao.loadUserPreferences(userName)
                .map((List<Preference> pl) -> {
                    if (preferenceMap == null) {
                        preferenceMap = new HashMap<>();
                    } else {
                        preferenceMap.clear();
                    }
                    for (Preference p : pl) {
                        preferenceMap.put(p.getCode(), p.getValue());
                    }

                    return preferenceMap;
                });
    }


    public Flowable<String> getPreference(String code) {
        return getPreferenceMap().map(pm -> pm.get(code));
    }

    public Flowable<String> getBaseVocabulary() {
        return getPreference(PC_BASE_VOCABULARY);
    }


    public void setBaseVocabulary(String categoryCode) {
        setPreference(PC_BASE_VOCABULARY, categoryCode);
    }


    public void setPreference(String code, String value) {
        if (preferenceMap != null) {
            preferenceMap.put(code, value);
        }
        Preference p = new Preference();
        setupNewUserData(p);
        p.setCode(code);
        p.setValue(value);
        unsavedPreference.add(p);
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
